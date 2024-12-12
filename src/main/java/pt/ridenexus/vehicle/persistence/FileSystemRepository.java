package pt.ridenexus.vehicle.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pt.ridenexus.vehicle.persistence.exception.FileDBAccessException;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Primary
@Repository
public class FileSystemRepository implements VehicleRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemRepository.class);
    private static Long idSequence;
    private static final Map<Long, Vehicle> VEHICLES = new LinkedHashMap<>();
    private static final File SEQUENCE = new File("sequence");
    private static final File VEHICLES_DB = new File("vehiclesDB");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        try {
            if(SEQUENCE.exists() && VEHICLES_DB.exists()) {
                //both files exist - work with them
                BufferedReader seqReader = new BufferedReader(new FileReader(SEQUENCE));
                String valueSeq = seqReader.readLine();
                idSequence = Optional.ofNullable(valueSeq).map(Long::parseLong).orElse(0L);
                seqReader.close();

                BufferedReader dbReader = new BufferedReader(new FileReader(VEHICLES_DB));

                String line;
                while((line = dbReader.readLine()) != null) {
                    Vehicle vehicle = OBJECT_MAPPER.readValue(line, Vehicle.class);
                    VEHICLES.put(vehicle.getId(), vehicle);
                }

                dbReader.close();

            } else {
                //one or both files were deleted - recreate
                LOG.info("Recreating vehicles DB files");
                LOG.info("deleting sequence file, {}", SEQUENCE.delete());
                LOG.info("deleting vehicles DB file, {}", VEHICLES_DB.delete());

                if(SEQUENCE.createNewFile() && VEHICLES_DB.createNewFile()) {
                    LOG.info("Files were recreated");
                } else {
                    throw new RuntimeException("Missing vehicles DB files!");
                }

                idSequence = 0L;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Vehicle addVehicle(Vehicle v) {
        Long id = idSequence + 1;
        v.setId(id);
        boolean successOnInsert = addVehicleToDB(v);

        if(successOnInsert) {
            boolean successOnSequence = writeSequence(id);

            if(successOnSequence) {
                VEHICLES.put(id, v);
                ++idSequence;
                return VEHICLES.get(id);
            } else {
                removeVehicleFromDB(VEHICLES.size() - 1);
            }
        }
        throw new FileDBAccessException("Failed to add vehicle to FileDB");
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle v) {
        if(VEHICLES.containsKey(id)) {
            v.setId(id);
            int rowNumber = indexOf(id) + 1;
            boolean success = updateVehicleInDB(rowNumber, v);

            if(success) {
                VEHICLES.put(id, v);
                return VEHICLES.get(id);
            }

            throw new FileDBAccessException("Failed to update vehicle in FileDB");
        } else {
            return null;
        }
    }

    @Override
    public Long removeVehicle(Long id) {
        int rowNumber = indexOf(id) + 1;
        boolean success = removeVehicleFromDB(rowNumber);

        if(success) {
            return Optional.ofNullable(VEHICLES.remove(id))
                    .map(Vehicle::getId)
                    .orElse(null);
        }

        throw new FileDBAccessException("Failed to remove vehicle from FileDB");
    }

    @Override
    public List<Vehicle> getVehicles() {
        return VEHICLES.values().stream().toList();
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return VEHICLES.get(id);
    }

    private static boolean writeSequence(Long sequence) {
        try(BufferedWriter fw = new BufferedWriter(new FileWriter(SEQUENCE))) {
            fw.write(sequence.toString());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean addVehicleToDB(Vehicle v) {
        try(BufferedWriter fw = new BufferedWriter(new FileWriter(VEHICLES_DB, true))) {
            fw.append(OBJECT_MAPPER.writeValueAsString(v)).append(System.lineSeparator());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean updateVehicleInDB(int n, Vehicle v) {
        File tempDbFile = new File("tempVehiclesDB");
        BufferedReader rd;
        BufferedWriter wr;
        try {
            rd = new BufferedReader(new FileReader(VEHICLES_DB));
            wr = new BufferedWriter(new FileWriter(tempDbFile));

            String line;
            int current = 0;
            while((line = rd.readLine()) != null) {
                wr.append(++current == n ? OBJECT_MAPPER.writeValueAsString(v) : line).append(System.lineSeparator());
            }
            rd.close();
            wr.close();

            Files.move(tempDbFile.toPath(), VEHICLES_DB.toPath(), StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static boolean removeVehicleFromDB(int n) {
        File tempDbFile = new File("tempVehiclesDB");
        BufferedReader rd;
        BufferedWriter wr;

        try {
            rd = new BufferedReader(new FileReader(VEHICLES_DB));
            wr = new BufferedWriter(new FileWriter(tempDbFile));

            String line;
            int current = 0;
            while((line = rd.readLine()) != null) {
                if(++current != n) {
                    wr.append(line).append(System.lineSeparator());
                }
            }
            rd.close();
            wr.close();

            Files.move(tempDbFile.toPath(), VEHICLES_DB.toPath(), StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static int indexOf(Long sequence) {
        List<Long> list = VEHICLES.keySet().stream().toList();
        return list.indexOf(sequence);
    }
}

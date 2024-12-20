package pt.ridenexus.vehicle.it.fixtures;

import java.util.List;

public class GraphQLDocuments {

    public static String addVehicle() {
        return """
            mutation AddVehicle($vehicle: AddVehicle) {
              addVehicle(vehicle: $vehicle) {
                id
              }
            }
        """;
    }

    public static String removeVehicle() {
        return """
            mutation RemoveVehicle($id: ID) {
              removeVehicle(id: $id)
            }
        """;
    }

    public static String getAllVehicles(List<String> fields) {
        return """
            query GetVehicles {
              getVehicles(pageNumber: 0, pageSize: 100) {
                content {
                  {fields}
                }
                pageInfo {
                  pageNumber
                  pageSize
                  totalElements
                }
              }
            }
        """.replace("{fields}", String.join("\n", fields));
    }

    public static String getById(List<String> fields) {
        return """
            query GetVehicleById($id: ID) {
              getVehicleById(id: $id) {
                {fields}
              }
            }
        """.replace("{fields}", String.join("\n", fields));
    }

    public static String updateVehicle() {
        return """
            mutation UpdateVehicle($id: ID, $vehicle: UpdateVehicle) {
              updateVehicle(id: $id, vehicle: $vehicle) {
                id
              }
            }
        """;
    }
}

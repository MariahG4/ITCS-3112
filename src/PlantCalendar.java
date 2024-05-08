import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Plant {
    private String name;
    private String careInstructions;
    private int wateringFrequency;

    public Plant(String name, String careInstructions, int wateringFrequency) {
        this.name = name;
        this.careInstructions = careInstructions;
        this.wateringFrequency = wateringFrequency;
    }

    public String getName() {
        return name;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public int getWateringFrequency() {
        return wateringFrequency;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public void setWateringFrequency(int wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }
}

public class PlantCalendar {
    private TreeMap<Date, Plant> calendar;

    public PlantCalendar() {
        calendar = new TreeMap<>();
    }

    public void addPlant(Date date, Plant plant) {
        calendar.put(date, plant);
    }

    public void displayCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> editedPlants = new HashSet<>();
        for (Map.Entry<Date, Plant> entry : calendar.entrySet()) {
            if (!editedPlants.contains(entry.getValue().getName())) {
                System.out
                        .println("Start Date: " + sdf.format(entry.getKey()) + ", Plant: " + entry.getValue().getName()
                                + ", Care Instructions: " + entry.getValue().getCareInstructions());
                printCareDates(entry.getKey(), entry.getValue().getWateringFrequency());
                editedPlants.add(entry.getValue().getName());
                System.out.println();
            }
        }
    }

    private void printCareDates(Date startDate, int wateringFrequency) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        System.out.println("   Next Care Date: " + sdf.format(startDate));
        for (int i = 1; i < 8; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, wateringFrequency);
            System.out.println("   Next Care Date: " + sdf.format(calendar.getTime()));
        }
    }

    public void deletePlant(String plantName) {
        Iterator<Map.Entry<Date, Plant>> iterator = calendar.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Date, Plant> entry = iterator.next();
            if (entry.getValue().getName().equalsIgnoreCase(plantName)) {
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PlantCalendar plantCalendar = new PlantCalendar();

        System.out.println("Welcome to Plant Calendar!");

        // Enter information for plant to be added
        while (true) {
            System.out.println(
                    "\nEnter plant details (name, care instructions, care frequency) or type 'quit' to exit:");
            System.out.print("Plant Name (and unique identifying number): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("quit")) {
                break;
            }
            System.out.print("Care Instructions: ");
            String careInstructions = scanner.nextLine();

            System.out.print("Care Frequency (in days): ");
            int wateringFrequency = scanner.nextInt();
            scanner.nextLine();

            System.out.print("First Care Date (yyyy-MM-dd): ");
            String firstWateringDateString = scanner.nextLine();
            Date firstWateringDate = null;
            try {
                firstWateringDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstWateringDateString);
            } catch (ParseException e) {
                System.out.println("Invalid date format! Please enter date in yyyy-MM-dd format.");
                continue;
            }

            Plant plant = new Plant(name, careInstructions, wateringFrequency);
            plantCalendar.addPlant(firstWateringDate, plant);
        }

        System.out.println("\nPlant Calendar:");
        plantCalendar.displayCalendar();

        // Allow editing of care instructions
        System.out.println("\nDo you want to edit care instructions for any plant? (yes/no)");
        String editChoice = scanner.nextLine();
        if (editChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter the name of the plant for which you want to edit care instructions: ");
            String plantNameToEdit = scanner.nextLine();

            // Find the plant to edit
            Plant plantToEdit = null;
            for (Map.Entry<Date, Plant> entry : plantCalendar.calendar.entrySet()) {
                if (entry.getValue().getName().equalsIgnoreCase(plantNameToEdit)) {
                    plantToEdit = entry.getValue();
                    break;
                }
            }

            if (plantToEdit != null) {
                System.out.print("Enter new care instructions: ");
                String newCareInstructions = scanner.nextLine();
                plantToEdit.setCareInstructions(newCareInstructions);

                System.out.print("Enter new care frequency (in days): ");
                int newWateringFrequency = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                plantToEdit.setWateringFrequency(newWateringFrequency);

                System.out.print("Enter new first care date (yyyy-MM-dd): ");
                String newFirstWateringDateString = scanner.nextLine();
                Date newFirstWateringDate = null;
                try {
                    newFirstWateringDate = new SimpleDateFormat("yyyy-MM-dd").parse(newFirstWateringDateString);
                } catch (ParseException e) {
                    System.out.println("Invalid date format! Please enter date in yyyy-MM-dd format.");
                    return;
                }

                // Removal of old entries for the edited plant
                ArrayList<Date> keysToRemove = new ArrayList<>();
                for (Map.Entry<Date, Plant> entry : plantCalendar.calendar.entrySet()) {
                    if (entry.getValue() == plantToEdit) {
                        keysToRemove.add(entry.getKey());
                    }
                }
                for (Date key : keysToRemove) {
                    plantCalendar.calendar.remove(key);
                }

                // Add updated entry with new watering dates
                plantCalendar.addPlant(newFirstWateringDate, plantToEdit);

                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(newFirstWateringDate);
                Date newNextWateringDate = newFirstWateringDate;
                for (int i = 0; i < 6; i++) {
                    newCalendar.add(Calendar.DAY_OF_MONTH, plantToEdit.getWateringFrequency());
                    newNextWateringDate = newCalendar.getTime();
                    plantCalendar.addPlant(newNextWateringDate, plantToEdit);
                }

                System.out.println("Plant details updated successfully!");
            } else {
                System.out.println("Plant not found in the calendar!");
            }
        }

        // Ask if the user wants to delete a plant
        System.out.println("\nDo you want to delete any plant from the calendar? (yes/no)");
        String deleteChoice = scanner.nextLine();
        if (deleteChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter the name of the plant you want to delete: ");
            String plantNameToDelete = scanner.nextLine();
            plantCalendar.deletePlant(plantNameToDelete);
            System.out.println("\nUpdated Plant Calendar after deleting:");
            plantCalendar.displayCalendar();
        }
    }
}

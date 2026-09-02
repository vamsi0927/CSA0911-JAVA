
import java.util.*;

/*
 * Smart Hospital Patient Appointment, Pharmacy Inventory
 * and Alert Notification System
 *
 * Core Java implementation for CSA09 - Programming in Java
 */

public class SmartHospitalSystem {

    // ---------- Custom Exceptions ----------

    static class InvalidPatientException extends Exception {
        InvalidPatientException(String message) {
            super(message);
        }
    }

    static class DuplicateAppointmentException extends Exception {
        DuplicateAppointmentException(String message) {
            super(message);
        }
    }

    static class OutOfStockException extends Exception {
        OutOfStockException(String message) {
            super(message);
        }
    }

    static class InvalidInputException extends Exception {
        InvalidInputException(String message) {
            super(message);
        }
    }

    // ---------- Patient Hierarchy ----------

    static abstract class Patient {
        private String patientId;
        private String name;
        private int age;
        private String phone;

        Patient(String patientId, String name, int age, String phone) {
            this.patientId = patientId;
            this.name = name;
            this.age = age;
            this.phone = phone;
        }

        public String getPatientId() { return patientId; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getPhone() { return phone; }

        public void setName(String name) { this.name = name; }
        public void setAge(int age) { this.age = age; }
        public void setPhone(String phone) { this.phone = phone; }

        public abstract double getConsultationFee();

        public abstract String getCategory();

        public void display() {
            System.out.printf("%-8s %-20s %-5d %-14s %-12s %.2f%n",
                    patientId, name, age, phone, getCategory(), getConsultationFee());
        }
    }

    static class GeneralPatient extends Patient {
        GeneralPatient(String id, String name, int age, String phone) {
            super(id, name, age, phone);
        }

        @Override
        public double getConsultationFee() {
            return 500.0;
        }

        @Override
        public String getCategory() {
            return "General";
        }
    }

    static class SeniorPatient extends Patient {
        SeniorPatient(String id, String name, int age, String phone) {
            super(id, name, age, phone);
        }

        @Override
        public double getConsultationFee() {
            return 250.0;
        }

        @Override
        public String getCategory() {
            return "Senior";
        }
    }

    static class EmergencyPatient extends Patient {
        EmergencyPatient(String id, String name, int age, String phone) {
            super(id, name, age, phone);
        }

        @Override
        public double getConsultationFee() {
            return 750.0;
        }

        @Override
        public String getCategory() {
            return "Emergency";
        }
    }

    // ---------- Doctor ----------

    static class Doctor {
        private String doctorId;
        private String name;
        private String specialization;
        private int maxAppointments;

        Doctor(String doctorId, String name, String specialization, int maxAppointments) {
            this.doctorId = doctorId;
            this.name = name;
            this.specialization = specialization;
            this.maxAppointments = maxAppointments;
        }

        public String getDoctorId() { return doctorId; }
        public String getName() { return name; }
        public String getSpecialization() { return specialization; }
        public int getMaxAppointments() { return maxAppointments; }

        public void display() {
            System.out.printf("%-8s %-20s %-20s %-8d%n",
                    doctorId, name, specialization, maxAppointments);
        }
    }

    // ---------- Appointment ----------

    static class Appointment {
        private String appointmentId;
        private String patientId;
        private String doctorId;
        private String date;
        private String time;
        private String status;

        Appointment(String appointmentId, String patientId, String doctorId,
                    String date, String time) {
            this.appointmentId = appointmentId;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.status = "BOOKED";
        }

        public String getAppointmentId() { return appointmentId; }
        public String getPatientId() { return patientId; }
        public String getDoctorId() { return doctorId; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }

        public void display() {
            System.out.printf("%-8s %-10s %-10s %-12s %-8s %-12s%n",
                    appointmentId, patientId, doctorId, date, time, status);
        }
    }

    // ---------- Medicine Hierarchy ----------

    static abstract class Medicine {
        private String medicineId;
        private String name;
        private double price;
        private int quantity;
        private int lowStockLevel;

        Medicine(String medicineId, String name, double price,
                 int quantity, int lowStockLevel) {
            this.medicineId = medicineId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.lowStockLevel = lowStockLevel;
        }

        public String getMedicineId() { return medicineId; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public int getLowStockLevel() { return lowStockLevel; }

        public void setPrice(double price) { this.price = price; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public abstract String getType();

        public void display() {
            System.out.printf("%-8s %-20s %-15s %-10.2f %-8d %-10d%n",
                    medicineId, name, getType(), price, quantity, lowStockLevel);
        }
    }

    static class Tablet extends Medicine {
        Tablet(String id, String name, double price, int quantity, int lowStock) {
            super(id, name, price, quantity, lowStock);
        }

        @Override
        public String getType() {
            return "Tablet";
        }
    }

    static class Syrup extends Medicine {
        Syrup(String id, String name, double price, int quantity, int lowStock) {
            super(id, name, price, quantity, lowStock);
        }

        @Override
        public String getType() {
            return "Syrup";
        }
    }

    static class Injection extends Medicine {
        Injection(String id, String name, double price, int quantity, int lowStock) {
            super(id, name, price, quantity, lowStock);
        }

        @Override
        public String getType() {
            return "Injection";
        }
    }

    // ---------- Inventory ----------

    static class Inventory {
        private final HashMap<String, Medicine> medicines = new HashMap<>();
        private final Hashtable<String, Integer> issuedRecords = new Hashtable<>();

        public synchronized void addMedicine(Medicine medicine) {
            medicines.put(medicine.getMedicineId(), medicine);
        }

        public synchronized Medicine getMedicine(String id) {
            return medicines.get(id);
        }

        public synchronized void displayAll() {
            if (medicines.isEmpty()) {
                System.out.println("No medicines available.");
                return;
            }

            System.out.println("\n---------------- PHARMACY INVENTORY ----------------");
            System.out.printf("%-8s %-20s %-15s %-10s %-8s %-10s%n",
                    "ID", "Name", "Type", "Price", "Qty", "LowLevel");

            Iterator<Medicine> iterator = medicines.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().display();
            }
        }

        public synchronized void issueMedicine(String id, int amount)
                throws OutOfStockException, InvalidInputException {

            if (amount <= 0) {
                throw new InvalidInputException("Medicine quantity must be positive.");
            }

            Medicine medicine = medicines.get(id);

            if (medicine == null) {
                throw new InvalidInputException("Medicine ID not found.");
            }

            if (medicine.getQuantity() < amount) {
                throw new OutOfStockException(
                        "Insufficient stock for " + medicine.getName() +
                        ". Available: " + medicine.getQuantity());
            }

            medicine.setQuantity(medicine.getQuantity() - amount);

            int old = issuedRecords.containsKey(id) ? issuedRecords.get(id) : 0;
            issuedRecords.put(id, old + amount);
        }

        public synchronized void restockMedicine(String id, int amount)
                throws InvalidInputException {

            if (amount <= 0) {
                throw new InvalidInputException("Restock quantity must be positive.");
            }

            Medicine medicine = medicines.get(id);

            if (medicine == null) {
                throw new InvalidInputException("Medicine ID not found.");
            }

            medicine.setQuantity(medicine.getQuantity() + amount);
        }

        public synchronized List<Medicine> getLowStockMedicines() {
            List<Medicine> lowStock = new ArrayList<>();

            for (Medicine medicine : medicines.values()) {
                if (medicine.getQuantity() <= medicine.getLowStockLevel()) {
                    lowStock.add(medicine);
                }
            }

            return lowStock;
        }

        public synchronized void updateMedicine(String id, double price, int quantity)
                throws InvalidInputException {

            Medicine medicine = medicines.get(id);

            if (medicine == null) {
                throw new InvalidInputException("Medicine ID not found.");
            }

            if (price < 0 || quantity < 0) {
                throw new InvalidInputException("Price and quantity cannot be negative.");
            }

            medicine.setPrice(price);
            medicine.setQuantity(quantity);
        }

        public synchronized void inventoryReport() {
            System.out.println("\n================ INVENTORY REPORT ================");

            int totalStock = 0;
            double stockValue = 0;

            for (Medicine medicine : medicines.values()) {
                totalStock += medicine.getQuantity();
                stockValue += medicine.getQuantity() * medicine.getPrice();
            }

            System.out.println("Different medicines : " + medicines.size());
            System.out.println("Total stock units   : " + totalStock);
            System.out.printf("Current stock value  : Rs. %.2f%n", stockValue);

            System.out.println("\nIssued Medicine Records:");
            if (issuedRecords.isEmpty()) {
                System.out.println("No medicines issued.");
            } else {
                for (Map.Entry<String, Integer> entry : issuedRecords.entrySet()) {
                    Medicine medicine = medicines.get(entry.getKey());
                    String name = medicine == null ? entry.getKey() : medicine.getName();
                    System.out.println(name + " -> " + entry.getValue() + " unit(s)");
                }
            }

            List<Medicine> lowStock = getLowStockMedicines();

            System.out.println("\nLow Stock Medicines:");
            if (lowStock.isEmpty()) {
                System.out.println("No low-stock medicines.");
            } else {
                for (Medicine medicine : lowStock) {
                    System.out.println("- " + medicine.getName() +
                            " (remaining: " + medicine.getQuantity() + ")");
                }
            }
        }
    }

    // ---------- Notification with Inter-thread Communication ----------

    static class Notification {
        private String message;
        private String type;

        Notification(String type, String message) {
            this.type = type;
            this.message = message;
        }

        public void display() {
            System.out.println("[NOTIFICATION - " + type + "] " + message);
        }
    }

    static class NotificationQueue {
        private final Queue<Notification> queue = new LinkedList<>();

        public synchronized void add(Notification notification) {
            queue.offer(notification);
            notifyAll();
        }

        public synchronized Notification take() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            return queue.poll();
        }
    }

    static class NotificationDispatcher extends Thread {
        private final NotificationQueue queue;
        private volatile boolean running = true;

        NotificationDispatcher(NotificationQueue queue) {
            this.queue = queue;
            setName("Notification-Dispatcher");
            setPriority(Thread.NORM_PRIORITY + 1);
        }

        public void stopDispatcher() {
            running = false;
            interrupt();
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Notification notification = queue.take();
                    notification.display();
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // Thread is being stopped or waiting for a notification.
                }
            }
        }
    }

    // ---------- Hospital Manager ----------

    static class HospitalManager {
        private final ArrayList<Patient> patients = new ArrayList<>();
        private final ArrayList<Doctor> doctors = new ArrayList<>();
        private final ArrayList<Appointment> appointments = new ArrayList<>();

        // Set prevents duplicate appointment keys.
        private final Set<String> appointmentKeys = new HashSet<>();

        // HashMap provides quick patient lookup.
        private final HashMap<String, Patient> patientMap = new HashMap<>();

        // Hashtable demonstrates synchronized legacy collection.
        private final Hashtable<String, Doctor> doctorTable = new Hashtable<>();

        private final Map<String, Queue<String>> waitlists = new HashMap<>();

        private final Inventory inventory = new Inventory();
        private final NotificationQueue notificationQueue = new NotificationQueue();
        private final NotificationDispatcher dispatcher =
                new NotificationDispatcher(notificationQueue);

        public HospitalManager() {
            dispatcher.start();
        }

        public void shutdown() {
            dispatcher.stopDispatcher();
        }

        // ----- Patient operations -----

        public synchronized void registerPatient(Patient patient)
                throws InvalidInputException {

            if (patient.getPatientId() == null ||
                    patient.getPatientId().trim().isEmpty()) {
                throw new InvalidInputException("Patient ID cannot be empty.");
            }

            if (patient.getAge() <= 0 || patient.getAge() > 120) {
                throw new InvalidInputException("Invalid patient age.");
            }

            if (patientMap.containsKey(patient.getPatientId())) {
                throw new InvalidInputException("Duplicate Patient ID.");
            }

            patients.add(patient);
            patientMap.put(patient.getPatientId(), patient);

            System.out.println("Patient registered successfully.");
        }

        public Patient findPatient(String id) throws InvalidPatientException {
            Patient patient = patientMap.get(id);

            if (patient == null) {
                throw new InvalidPatientException(
                        "Invalid Patient ID: " + id);
            }

            return patient;
        }

        public synchronized void displayPatients() {
            System.out.println("\n---------------- PATIENTS ----------------");
            System.out.printf("%-8s %-20s %-5s %-14s %-12s %s%n",
                    "ID", "Name", "Age", "Phone", "Category", "Fee");

            Iterator<Patient> iterator = patients.iterator();
            while (iterator.hasNext()) {
                iterator.next().display();
            }
        }

        public synchronized void updatePatient(String id, String name,
                                                int age, String phone)
                throws InvalidPatientException, InvalidInputException {

            Patient patient = findPatient(id);

            if (age <= 0 || age > 120) {
                throw new InvalidInputException("Invalid patient age.");
            }

            patient.setName(name);
            patient.setAge(age);
            patient.setPhone(phone);

            System.out.println("Patient record updated successfully.");
        }

        // ----- Doctor operations -----

        public synchronized void registerDoctor(Doctor doctor)
                throws InvalidInputException {

            if (doctorTable.containsKey(doctor.getDoctorId())) {
                throw new InvalidInputException("Duplicate Doctor ID.");
            }

            if (doctor.getMaxAppointments() <= 0) {
                throw new InvalidInputException(
                        "Maximum appointments must be positive.");
            }

            doctors.add(doctor);
            doctorTable.put(doctor.getDoctorId(), doctor);
            waitlists.put(doctor.getDoctorId(), new LinkedList<>());

            System.out.println("Doctor registered successfully.");
        }

        public synchronized Doctor findDoctor(String id)
                throws InvalidInputException {

            Doctor doctor = doctorTable.get(id);

            if (doctor == null) {
                throw new InvalidInputException("Doctor ID not found.");
            }

            return doctor;
        }

        public synchronized void displayDoctors() {
            System.out.println("\n---------------- DOCTORS ----------------");
            System.out.printf("%-8s %-20s %-20s %-8s%n",
                    "ID", "Name", "Specialization", "MaxSlots");

            for (Doctor doctor : doctors) {
                doctor.display();
            }
        }

        // ----- Appointment operations -----

        private String appointmentKey(String patientId, String doctorId,
                                      String date, String time) {
            return patientId + "|" + doctorId + "|" + date + "|" + time;
        }

        private int bookedCount(String doctorId, String date) {
            int count = 0;

            for (Appointment appointment : appointments) {
                if (appointment.getDoctorId().equals(doctorId) &&
                        appointment.getDate().equals(date) &&
                        appointment.getStatus().equals("BOOKED")) {
                    count++;
                }
            }

            return count;
        }

        public synchronized void bookAppointment(String appointmentId,
                                                  String patientId,
                                                  String doctorId,
                                                  String date,
                                                  String time)
                throws InvalidPatientException, InvalidInputException,
                DuplicateAppointmentException {

            findPatient(patientId);
            findDoctor(doctorId);

            String key = appointmentKey(patientId, doctorId, date, time);

            if (appointmentKeys.contains(key)) {
                throw new DuplicateAppointmentException(
                        "Duplicate appointment for this patient and time.");
            }

            Doctor doctor = doctorTable.get(doctorId);
            int booked = bookedCount(doctorId, date);

            if (booked >= doctor.getMaxAppointments()) {
                Queue<String> waitlist = waitlists.get(doctorId);
                waitlist.offer(patientId);

                notificationQueue.add(new Notification(
                        "WAITLIST",
                        "Patient " + patientId +
                        " added to Dr. " + doctor.getName() + "'s waitlist."));

                System.out.println("Doctor is fully booked.");
                System.out.println("Patient added to waitlist.");
                return;
            }

            Appointment appointment = new Appointment(
                    appointmentId, patientId, doctorId, date, time);

            appointments.add(appointment);
            appointmentKeys.add(key);

            notificationQueue.add(new Notification(
                    "APPOINTMENT",
                    "Appointment " + appointmentId +
                    " booked for patient " + patientId +
                    " with Dr. " + doctor.getName() +
                    " on " + date + " at " + time + "."));

            System.out.println("Appointment booked successfully.");
        }

        public synchronized void cancelAppointment(String appointmentId)
                throws InvalidInputException {

            ListIterator<Appointment> iterator = appointments.listIterator();

            while (iterator.hasNext()) {
                Appointment appointment = iterator.next();

                if (appointment.getAppointmentId().equals(appointmentId) &&
                        appointment.getStatus().equals("BOOKED")) {

                    appointment.setStatus("CANCELLED");

                    String key = appointmentKey(
                            appointment.getPatientId(),
                            appointment.getDoctorId(),
                            appointment.getDate(),
                            appointment.getTime());

                    appointmentKeys.remove(key);

                    promoteFromWaitlist(appointment.getDoctorId(),
                            appointment.getDate(),
                            appointment.getTime());

                    notificationQueue.add(new Notification(
                            "CANCELLATION",
                            "Appointment " + appointmentId + " cancelled."));

                    System.out.println("Appointment cancelled successfully.");
                    return;
                }
            }

            throw new InvalidInputException(
                    "Active appointment ID not found.");
        }

        private void promoteFromWaitlist(String doctorId, String date,
                                         String time) {

            Queue<String> waitlist = waitlists.get(doctorId);

            if (waitlist == null || waitlist.isEmpty()) {
                return;
            }

            String patientId = waitlist.poll();

            String newId = "AUTO-" + System.currentTimeMillis() % 100000;

            Appointment appointment = new Appointment(
                    newId, patientId, doctorId, date, time);

            appointments.add(appointment);

            appointmentKeys.add(
                    appointmentKey(patientId, doctorId, date, time));

            Doctor doctor = doctorTable.get(doctorId);

            notificationQueue.add(new Notification(
                    "WAITLIST",
                    "Waitlisted patient " + patientId +
                    " promoted to appointment " + newId +
                    " with Dr. " + doctor.getName() + "."));
        }

        public synchronized void displayAppointments() {
            System.out.println("\n---------------- APPOINTMENTS ----------------");
            System.out.printf("%-8s %-10s %-10s %-12s %-8s %-12s%n",
                    "ApptID", "Patient", "Doctor", "Date", "Time", "Status");

            if (appointments.isEmpty()) {
                System.out.println("No appointments.");
                return;
            }

            ListIterator<Appointment> iterator =
                    appointments.listIterator();

            while (iterator.hasNext()) {
                iterator.next().display();
            }
        }

        public synchronized void patientVisitReport() {
            System.out.println("\n================ PATIENT VISIT REPORT ================");

            HashMap<String, Integer> visitCount = new HashMap<>();

            for (Appointment appointment : appointments) {
                if (appointment.getStatus().equals("BOOKED")) {
                    String patientId = appointment.getPatientId();
                    int count = visitCount.containsKey(patientId)
                            ? visitCount.get(patientId) : 0;
                    visitCount.put(patientId, count + 1);
                }
            }

            if (visitCount.isEmpty()) {
                System.out.println("No active visits.");
                return;
            }

            for (Map.Entry<String, Integer> entry : visitCount.entrySet()) {
                Patient patient = patientMap.get(entry.getKey());
                String name = patient == null ? entry.getKey() : patient.getName();

                System.out.println(entry.getKey() + " - " + name +
                        " : " + entry.getValue() + " active visit(s)");
            }
        }

        // ----- Pharmacy operations -----

        public void addMedicine(Medicine medicine) {
            inventory.addMedicine(medicine);
            System.out.println("Medicine added successfully.");
        }

        public void displayInventory() {
            inventory.displayAll();
        }

        public void issueMedicine(String id, int quantity)
                throws OutOfStockException, InvalidInputException {

            inventory.issueMedicine(id, quantity);

            Medicine medicine = inventory.getMedicine(id);

            notificationQueue.add(new Notification(
                    "PHARMACY",
                    quantity + " unit(s) of " + medicine.getName() +
                    " issued successfully."));

            if (medicine.getQuantity() <= medicine.getLowStockLevel()) {
                notificationQueue.add(new Notification(
                        "LOW STOCK",
                        medicine.getName() +
                        " is low on stock. Remaining: " +
                        medicine.getQuantity()));
            }
        }

        public void restockMedicine(String id, int quantity)
                throws InvalidInputException {

            inventory.restockMedicine(id, quantity);

            Medicine medicine = inventory.getMedicine(id);

            notificationQueue.add(new Notification(
                    "PHARMACY",
                    medicine.getName() +
                    " restocked. Current quantity: " +
                    medicine.getQuantity()));
        }

        public void updateMedicine(String id, double price, int quantity)
                throws InvalidInputException {

            inventory.updateMedicine(id, price, quantity);

            notificationQueue.add(new Notification(
                    "PHARMACY",
                    "Medicine " + id + " updated successfully."));
        }

        public void inventoryReport() {
            inventory.inventoryReport();
        }

        public void checkLowStock() {
            List<Medicine> lowStock = inventory.getLowStockMedicines();

            System.out.println("\n---------------- LOW STOCK ALERT ----------------");

            if (lowStock.isEmpty()) {
                System.out.println("No low-stock medicines.");
                return;
            }

            for (Medicine medicine : lowStock) {
                System.out.println(medicine.getMedicineId() + " - " +
                        medicine.getName() + " : " +
                        medicine.getQuantity() + " remaining");

                notificationQueue.add(new Notification(
                        "LOW STOCK",
                        medicine.getName() +
                        " requires restocking."));
            }
        }

        // ----- Concurrent booking simulation -----

        public void simulateConcurrentBooking(String patientId,
                                               String doctorId,
                                               String date) {

            Thread bookingThread1 = new Thread(() -> {
                try {
                    bookAppointment(
                            "TH-" + System.currentTimeMillis() % 10000,
                            patientId, doctorId, date, "10:00");
                } catch (Exception e) {
                    System.out.println("Booking Thread 1: " + e.getMessage());
                }
            }, "Booking-Thread-1");

            Thread bookingThread2 = new Thread(() -> {
                try {
                    bookAppointment(
                            "TH-" + (System.currentTimeMillis() + 1) % 10000,
                            patientId, doctorId, date, "11:00");
                } catch (Exception e) {
                    System.out.println("Booking Thread 2: " + e.getMessage());
                }
            }, "Booking-Thread-2");

            bookingThread1.setPriority(Thread.MAX_PRIORITY);
            bookingThread2.setPriority(Thread.NORM_PRIORITY);

            bookingThread1.start();
            bookingThread2.start();

            try {
                bookingThread1.join();
                bookingThread2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Concurrent booking simulation completed.");
        }
    }

    // ---------- Input Helpers ----------

    static class Input {
        private static final Scanner sc = new Scanner(System.in);

        static String text(String message) {
            System.out.print(message);
            return sc.nextLine().trim();
        }

        static int integer(String message) throws InvalidInputException {
            String value = text(message);

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Please enter a valid integer.");
            }
        }

        static double decimal(String message) throws InvalidInputException {
            String value = text(message);

            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Please enter a valid number.");
            }
        }
    }

    // ---------- Menu ----------

    static void printMenu() {
        System.out.println("\n======================================================");
        System.out.println("       SMART HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("======================================================");
        System.out.println("1.  Register Patient");
        System.out.println("2.  Display Patients");
        System.out.println("3.  Update Patient");
        System.out.println("4.  Register Doctor");
        System.out.println("5.  Display Doctors");
        System.out.println("6.  Book Appointment");
        System.out.println("7.  Cancel Appointment");
        System.out.println("8.  Display Appointments");
        System.out.println("9.  Add Medicine");
        System.out.println("10. Display Pharmacy Inventory");
        System.out.println("11. Issue Medicine");
        System.out.println("12. Restock Medicine");
        System.out.println("13. Update Medicine");
        System.out.println("14. Check Low Stock");
        System.out.println("15. Patient Visit Report");
        System.out.println("16. Pharmacy Inventory Report");
        System.out.println("17. Search Patient");
        System.out.println("18. Concurrent Booking Demo");
        System.out.println("19. Exit");
        System.out.println("======================================================");
    }

    static Patient createPatient() throws InvalidInputException {
        String id = Input.text("Patient ID: ");
        String name = Input.text("Patient Name: ");
        int age = Input.integer("Age: ");
        String phone = Input.text("Phone: ");

        System.out.println("Patient Category:");
        System.out.println("1. General");
        System.out.println("2. Senior");
        System.out.println("3. Emergency");

        int category = Input.integer("Choose category: ");

        switch (category) {
            case 1:
                return new GeneralPatient(id, name, age, phone);
            case 2:
                return new SeniorPatient(id, name, age, phone);
            case 3:
                return new EmergencyPatient(id, name, age, phone);
            default:
                throw new InvalidInputException("Invalid patient category.");
        }
    }

    static Medicine createMedicine() throws InvalidInputException {
        String id = Input.text("Medicine ID: ");
        String name = Input.text("Medicine Name: ");
        double price = Input.decimal("Price: ");
        int quantity = Input.integer("Quantity: ");
        int lowStock = Input.integer("Low-stock level: ");

        if (price < 0 || quantity < 0 || lowStock < 0) {
            throw new InvalidInputException(
                    "Price, quantity and low-stock level cannot be negative.");
        }

        System.out.println("Medicine Type:");
        System.out.println("1. Tablet");
        System.out.println("2. Syrup");
        System.out.println("3. Injection");

        int type = Input.integer("Choose type: ");

        switch (type) {
            case 1:
                return new Tablet(id, name, price, quantity, lowStock);
            case 2:
                return new Syrup(id, name, price, quantity, lowStock);
            case 3:
                return new Injection(id, name, price, quantity, lowStock);
            default:
                throw new InvalidInputException("Invalid medicine type.");
        }
    }

    static void runApplication() {
        HospitalManager manager = new HospitalManager();

        try {
            // Sample records make the system immediately testable.
            manager.registerPatient(
                    new GeneralPatient("P101", "Anirudh", 21, "9876543210"));
            manager.registerPatient(
                    new SeniorPatient("P102", "Ravi", 68, "9876501234"));

            manager.registerDoctor(
                    new Doctor("D101", "Dr. Priya", "Cardiology", 2));
            manager.registerDoctor(
                    new Doctor("D102", "Dr. Kumar", "General Medicine", 3));

            manager.addMedicine(
                    new Tablet("M101", "Paracetamol", 25, 50, 10));
            manager.addMedicine(
                    new Syrup("M102", "Cough Syrup", 90, 8, 5));
            manager.addMedicine(
                    new Injection("M103", "Insulin", 250, 12, 4));

        } catch (Exception e) {
            System.out.println("Initial data error: " + e.getMessage());
        }

        boolean running = true;

        while (running) {
            printMenu();

            try {
                int choice = Input.integer("Enter choice: ");

                switch (choice) {

                    case 1:
                        manager.registerPatient(createPatient());
                        break;

                    case 2:
                        manager.displayPatients();
                        break;

                    case 3: {
                        String id = Input.text("Patient ID to update: ");
                        String name = Input.text("New name: ");
                        int age = Input.integer("New age: ");
                        String phone = Input.text("New phone: ");

                        manager.updatePatient(id, name, age, phone);
                        break;
                    }

                    case 4: {
                        String id = Input.text("Doctor ID: ");
                        String name = Input.text("Doctor Name: ");
                        String specialization = Input.text("Specialization: ");
                        int max = Input.integer("Maximum appointments: ");

                        manager.registerDoctor(
                                new Doctor(id, name, specialization, max));
                        break;
                    }

                    case 5:
                        manager.displayDoctors();
                        break;

                    case 6: {
                        String appointmentId =
                                Input.text("Appointment ID: ");
                        String patientId =
                                Input.text("Patient ID: ");
                        String doctorId =
                                Input.text("Doctor ID: ");
                        String date =
                                Input.text("Date (DD-MM-YYYY): ");
                        String time =
                                Input.text("Time (HH:MM): ");

                        manager.bookAppointment(
                                appointmentId, patientId, doctorId, date, time);
                        break;
                    }

                    case 7:
                        manager.cancelAppointment(
                                Input.text("Appointment ID: "));
                        break;

                    case 8:
                        manager.displayAppointments();
                        break;

                    case 9:
                        manager.addMedicine(createMedicine());
                        break;

                    case 10:
                        manager.displayInventory();
                        break;

                    case 11: {
                        String id = Input.text("Medicine ID: ");
                        int quantity = Input.integer("Quantity to issue: ");

                        manager.issueMedicine(id, quantity);
                        break;
                    }

                    case 12: {
                        String id = Input.text("Medicine ID: ");
                        int quantity = Input.integer("Quantity to restock: ");

                        manager.restockMedicine(id, quantity);
                        break;
                    }

                    case 13: {
                        String id = Input.text("Medicine ID: ");
                        double price = Input.decimal("New price: ");
                        int quantity = Input.integer("New quantity: ");

                        manager.updateMedicine(id, price, quantity);
                        break;
                    }

                    case 14:
                        manager.checkLowStock();
                        break;

                    case 15:
                        manager.patientVisitReport();
                        break;

                    case 16:
                        manager.inventoryReport();
                        break;

                    case 17: {
                        String id = Input.text("Patient ID to search: ");
                        Patient patient = manager.findPatient(id);

                        System.out.println("\nPatient found:");
                        patient.display();
                        break;
                    }

                    case 18: {
                        String patientId =
                                Input.text("Patient ID: ");
                        String doctorId =
                                Input.text("Doctor ID: ");
                        String date =
                                Input.text("Date (DD-MM-YYYY): ");

                        manager.simulateConcurrentBooking(
                                patientId, doctorId, date);
                        break;
                    }

                    case 19:
                        running = false;
                        break;

                    default:
                        throw new InvalidInputException(
                                "Please choose a menu option from 1 to 19.");
                }

            } catch (InvalidPatientException |
                     DuplicateAppointmentException |
                     OutOfStockException |
                     InvalidInputException e) {

                System.out.println("\nERROR: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("\nUnexpected error: " + e.getMessage());
            }
        }

        manager.shutdown();
        System.out.println("\nThank you for using Smart Hospital System.");
    }

    public static void main(String[] args) {
        runApplication();
    }
}

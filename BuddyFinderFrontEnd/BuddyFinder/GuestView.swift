import SwiftUI

struct Appointment {
    var name: String
    var date: Date
}

struct EventCreationView: View {
    @State private var appointmentName: String = ""
    @State private var appointmentDate: Date = Date()
    @State private var appointments: [Appointment] = []

    var body: some View {
        NavigationView {
            VStack {
                Form {
                    TextField("Appointment Name", text: $appointmentName)
                    DatePicker("Date", selection: $appointmentDate, displayedComponents: [.date, .hourAndMinute])
                }
                .padding()

                Button("Add Appointment") {
                    addAppointment()
                }
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)

                // List view to display appointments
                ScrollView {
                    ForEach(appointments, id: \.name) { appointment in
                        VStack(alignment: .leading) {
                            Text(appointment.name)
                                .font(.headline)
                            Text("Date: \(appointment.date, formatter: itemFormatter)")
                                .font(.subheadline)
                        }
                        .padding()
                    }
                }
            }
            .navigationBarTitle("Create Appointment")
        }
    }

    private func addAppointment() {
        let newAppointment = Appointment(name: appointmentName, date: appointmentDate)
        appointments.append(newAppointment)
        // Reset the fields
        appointmentName = ""
        appointmentDate = Date()
    }

    private var itemFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .long
        formatter.timeStyle = .short
        return formatter
    }
}

struct EventCreationView_Previews: PreviewProvider {
    static var previews: some View {
        EventCreationView()
    }
}

import SwiftUI
struct Event: Identifiable, Codable {

    var id = UUID()

    var name: String

    var startTime: Date

    var endTime: Date

}
struct FormattedEvent: Codable {

    var startTime: String

    var day: String

    var id: UUID

    var name: String

}

extension Event {

    var formattedEvent: FormattedEvent {

        let formatter = DateFormatter()

        formatter.dateFormat = "HH:mm"

        

        let dayFormatter = DateFormatter()

        dayFormatter.dateFormat = "E"

        

        return FormattedEvent(

            startTime: formatter.string(from: startTime),

            day: dayFormatter.string(from: startTime),

            id: id,

            name: name

        )

    }

}

class ScheduleModel: ObservableObject {

    @Published var events: [Event] = []

    

    func addEvent(event: Event) {

        events.append(event)

    }

    

    func deleteEvent(at index: Int) {

        events.remove(at: index)

    }

    

    func editEvent(at index: Int, newEvent: Event) {

        events[index] = newEvent

    }

    

    func getFormattedEvents() -> [FormattedEvent] {

        return events.map { $0.formattedEvent }

    }

}

struct ScheduleApp: App {

    var body: some Scene {

        WindowGroup {

            CalendarView()

        }

    }

}



struct CalendarView: View {

    @StateObject private var scheduleModel = ScheduleModel()

    @State private var newEventName = ""

    @State private var selectedDate = Date()

    @State private var selectedStartTime = Date()

    @State private var selectedEndTime = Date()

    @State private var isEditing = false

    @State private var editIndex: Int?

    @State private var navigateToHome = false // State variable for navigation


    var body: some View {

        NavigationView {

            VStack {

                DatePicker("Select Date", selection: $selectedDate, displayedComponents: [.date])

                    .datePickerStyle(CompactDatePickerStyle())

                    .padding()

                

                HStack {

                    Text("Start Time")

                    DatePicker("", selection: $selectedStartTime, displayedComponents: [.hourAndMinute])

                        .datePickerStyle(CompactDatePickerStyle())

                }

                .padding()

                

                HStack {

                    Text("End Time")

                    DatePicker("", selection: $selectedEndTime, displayedComponents: [.hourAndMinute])

                        .datePickerStyle(CompactDatePickerStyle())

                }

                .padding()

                

                List {

                    ForEach(scheduleModel.events) { event in

                        VStack(alignment: .leading) {

                            Text(event.name)

                                .font(.headline)

                            Text("Start Time: \(event.startTime, formatter: dateFormatter)")

                                .foregroundColor(.gray)

                            Text("End Time: \(event.endTime, formatter: dateFormatter)")

                                .foregroundColor(.gray)

                        }

                        .contextMenu {

                            Button("Edit") {

                                isEditing = true

                                editIndex = scheduleModel.events.firstIndex(where: { $0.id == event.id })

                                newEventName = event.name

                                selectedDate = event.startTime

                                selectedStartTime = event.startTime

                                selectedEndTime = event.endTime

                            }

                        }

                    }

                    .onDelete { indices in

                        scheduleModel.deleteEvent(at: indices.first!)

                    }

                }

                .listStyle(InsetGroupedListStyle())

                .navigationTitle("My Schedule")

                .toolbar {

                    ToolbarItem(placement: .navigationBarTrailing) {
                        


                        Button("Done") {

                            // Save event data using UserDefaults as an example

                            let formattedEvents = scheduleModel.getFormattedEvents()

                            let eventsData = try? JSONEncoder().encode(formattedEvents)

                            UserDefaults.standard.set(eventsData, forKey: "eventsDataKey")

                            

                            // Print out the formatted data

                            if let data = eventsData {

                                let jsonString = String(data: data, encoding: .utf8)

                                print("Formatted Data:\n\(jsonString ?? "Unable to convert data to string")")

                            }

                            
                            navigateToHome = true;
                            // Perform any action you want when the "Done" button is tapped
                                                        // Change to the next page

                        }

                    }

                }
                NavigationLink(destination: HomepageView(), isActive: $navigateToHome) {
                    EmptyView()
                }

                

                HStack {

                    TextField("New Event", text: $newEventName)

                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    Button(action: {

                        let newEvent = Event(name: newEventName, startTime: selectedDate.combine(with: selectedStartTime), endTime: selectedDate.combine(with: selectedEndTime))

                        if let index = editIndex {

                            scheduleModel.editEvent(at: index, newEvent: newEvent)

                        } else {

                            scheduleModel.addEvent(event: newEvent)

                        }

                        newEventName = ""

                        isEditing = false

                        editIndex = nil

                    }) {

                        Text("Add Event")

                            .padding()

                            .frame(maxWidth: .infinity)

                            .background(Color.blue)

                            .foregroundColor(.white)

                            .cornerRadius(8)

                    }

                }

                .padding()

            }

        }

    }
    private var dateFormatter: DateFormatter {

        let formatter = DateFormatter()

        formatter.dateStyle = .medium

        formatter.timeStyle = .short

        return formatter

    }

}

extension Date {
    func combine(with time: Date) -> Date {
        let calendar = Calendar.current
        let dateComponents = calendar.dateComponents([.year, .month, .day], from: self)
        let timeComponents = calendar.dateComponents([.hour, .minute, .second], from: time)
        
        return calendar.date(from: DateComponents(year: dateComponents.year,
                                                  month: dateComponents.month,
                                                  day: dateComponents.day,
                                                  hour: timeComponents.hour,
                                                  minute: timeComponents.minute,
                                                  second: timeComponents.second)) ?? self
    }
}


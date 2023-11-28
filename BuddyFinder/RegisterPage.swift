import SwiftUI

struct RegisterPage: View {
    @State private var email: String = ""
    @State private var username: String = ""
    @State private var password: String = ""
    @State private var shouldNavigate = false
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    var body: some View {
        NavigationView {
            VStack {
                TextField("Email", text: $email)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())

                TextField("Username", text: $username)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())

                SecureField("Password", text: $password)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())

                Button("Register") {
                    // Insert your registration logic here
                    // If registration is successful:
                    self.shouldNavigate = true
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.green)
                .cornerRadius(10)

                NavigationLink(destination: CalendarView(), isActive: $shouldNavigate) {
                    EmptyView()
                }

                Button("Back") {
                    presentationMode.wrappedValue.dismiss()
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.red)
                .cornerRadius(10)

                Spacer()
            }
            .padding()
            .navigationBarTitle("Register")
        }
    }
}

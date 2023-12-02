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
                    registerUser()
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
    
    func registerUser() {
        print("run function")
        
        guard let url = URL(string: "http://localhost:8080/BuddiestFounder/SignUp") else {
            return
        }
        
        let info: [String: String] = [
            "username": username,
            "password": password,
            "email": email
        ]
        
        print("Username: \(username)")
        print("Password: \(password)")
        print("Email: \(email)")

        
        var http = URLRequest(url: url)
        
        http.httpMethod = "POST"
        
        do {
            http.httpBody = try JSONSerialization.data(withJSONObject: info, options: .prettyPrinted)
        } catch let error {
            print(error.localizedDescription)
            return
        }
        
        http.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: http) { data, response, error in
               if let error = error {
                   print("Error: \(error)")
               } else if let data = data {
                   // Handle the response data as needed
                   print("Response: \(String(data: data, encoding: .utf8) ?? "")")

                   // Assuming the response indicates a successful registration
                   DispatchQueue.main.async {
                       self.shouldNavigate = true
                   }
               }
           }.resume()
        
        print("ran function")
    }
    
}

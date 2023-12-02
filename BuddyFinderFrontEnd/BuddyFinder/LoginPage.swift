import SwiftUI

struct LoginPage: View {
    @State private var username: String = ""
    @State private var password: String = ""
    @State private var shouldNavigate = false
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    var body: some View {
        NavigationView {
            VStack {
                TextField("Username", text: $username)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())

                SecureField("Password", text: $password)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                

                Button("Login") {
                    // Replace this with your actual login logic
                    // If login is successful:
                    print("clicked")
                loginUser()

                    
                    
//                    loginUser { result in
//                        if result == "Success" {
//                            // Code to execute when login is successful
//                            print("Login successful")
//                            self.shouldNavigate = true
//                        } else {
//                            // Code to execute when login is not successful
//                            print("Login failed: \(result)")
//                        }
//                    }
                    
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.blue)
                .cornerRadius(10)

                NavigationLink(destination: HomepageView(), isActive: $shouldNavigate) {
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
            .navigationBarTitle("Login")
        }
    }
    
    func loginUser() -> String  {
        print("run function")
        
        guard let url = URL(string: "http://localhost:8080/BuddiestFounder/Login") else {
            return "failure"
        }
        
        let info: [String: String] = [
            "username": username,
            "password": password,
        ]
        
        print("Username: \(username)")
        print("Password: \(password)")

        
        var http = URLRequest(url: url)
        
        http.httpMethod = "POST"
        
        do {
            http.httpBody = try JSONSerialization.data(withJSONObject: info, options: .prettyPrinted)
        } catch let error {
            print(error.localizedDescription)
            return "failure"
        }
        
        http.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        var response: String = ""

        URLSession.shared.dataTask(with: http) { data, _, error in
            if let error = error {
                print("Error: \(error)")
                let response = "failure"
            } else if let data = data {
                print("Response: \(String(data: data, encoding: .utf8) ?? "")")
                let response = String(data: data, encoding: .utf8) ?? ""
                if response == "{\"status\":\"success\"}" {
                    print("Login successful")
                    self.shouldNavigate = true
                }
                
            }
        }.resume()

        print("ran function")


        return response

    }
    
}

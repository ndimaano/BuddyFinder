import SwiftUI

struct ContentView: View {
    @State private var isLoginPresented = false
    @State private var isRegisterPresented = false
    @State private var isGuestPresented = false
    @State private var buddyName: String? // Modified to be an optional string

    var body: some View {
        NavigationView {
            VStack {
                Button("Login") {
                    isLoginPresented = true
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.blue)
                .cornerRadius(10)
                .sheet(isPresented: $isLoginPresented) {
                    LoginPage()
                }

                Spacer().frame(height: 20)

                Button("Register") {
                    isRegisterPresented = true
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.green)
                .cornerRadius(10)
                .sheet(isPresented: $isRegisterPresented) {
                    RegisterPage()
                }

                Button("Find Buddy") {
                    // Generate a random buddy
                    let generatedName = "User \(Int.random(in: 1...100))"
                    buddyName = generatedName
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.orange)
                .cornerRadius(10)

                // Conditional navigation link
                if let buddyName = buddyName {
                    NavigationLink(destination: ChatPage(userName: buddyName), isActive: Binding(
                        get: { self.buddyName != nil },
                        set: { _ in self.buddyName = nil }
                    )) {
                        EmptyView()
                    }
                }

                Spacer().frame(height: 20)

                Button("Guest") {
                    isGuestPresented = true
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.orange)
                .cornerRadius(10)
                .sheet(isPresented: $isGuestPresented) {
                    EventCreationView() // Replace with your GuestView
                }

                Spacer()
            }
            .padding()
            .navigationBarTitle("Buddy Finder")
        }
    }
}

// Dummy Views for LoginPage, RegisterPage, and EventCreationView

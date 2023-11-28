import SwiftUI

struct ChatPage: View {
    let userName: String
    @State private var message: String = ""
    var body: some View {
        VStack {
            Text("Chatting with \(userName)")
            // Add other chat UI elements as needed
            Spacer()
            
            HStack {
                TextField("Type a message...", text: $message)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding(.horizontal)
                
                Button(action: {
                    // Handle send message action
                    print("Message sent: \(message)")
                    message = ""
                }) {
                    Text("Send")
                        .padding(.vertical, 8)
                        .padding(.horizontal, 16)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
            }
            .padding(.bottom)
        }
        .padding()
        .navigationBarTitle("Chat")
    }
}

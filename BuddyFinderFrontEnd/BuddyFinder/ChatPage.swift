import SwiftUI

struct ChatPage: View {
    let userName: String
    @State private var message: String = ""
    @State private var messages: [String] = [] // Array to store messages

    var body: some View {
        VStack {
            Text("Chatting with \(userName)")
            .padding()

            // Display messages
            ScrollView {
                ForEach(messages, id: \.self) { msg in
                    Text(msg)
                        .padding()
                        .foregroundColor(.white)
                        .background(Color.blue)
                        .cornerRadius(10)
                        .frame(maxWidth: .infinity, alignment: .trailing) // Align to right
                        .padding(5)
                }
            }

            Spacer()

            // Message input and send button
            HStack {
                TextField("Type a message...", text: $message)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding(.horizontal)
                
                Button(action: sendMessage) {
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
        .navigationBarTitle("Chat")
    }

    private func sendMessage() {
        if !message.isEmpty {
            messages.append(message)
            message = ""
        }
    }
    
}

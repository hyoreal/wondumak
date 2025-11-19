import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import axios from '@/pages/api/axios';

interface ChatbotModalProps {
  onClose: () => void;
}

interface Message {
  sender: 'user' | 'bot';
  text: string;
}

// API 호출 함수
const fetchChatbotResponse = async (question: string) => {
  const { data } = await axios.post('/api/chatbot/ask', { question });
  return data.answer;
};

const ChatbotModal = ({ onClose }: ChatbotModalProps) => {
  const [inputMessage, setInputMessage] = useState('');
  const [chatHistory, setChatHistory] = useState<Message[]>([
    {
      sender: 'bot',
      text: '안녕하세요! 맥주에 대해 궁금한 점이 있으신가요? "피자와 어울리는 맥주 추천해줘" 와 같이 물어보세요!',
    },
  ]);

  const mutation = useMutation<string, Error, string>({
    mutationFn: fetchChatbotResponse,
    onSuccess: (data) => {
      setChatHistory((prev) => [...prev, { sender: 'bot', text: data }]);
    },
    onError: (error) => {
      setChatHistory((prev) => [
        ...prev,
        { sender: 'bot', text: `죄송합니다. 오류가 발생했습니다: ${error.message}` },
      ]);
    },
  });

  const handleSendMessage = () => {
    if (!inputMessage.trim()) return;

    // Add user message to chat history
    setChatHistory((prev) => [
      ...prev,
      { sender: 'user', text: inputMessage },
    ]);

    // Call the mutation
    mutation.mutate(inputMessage);

    // Clear input field
    setInputMessage('');
  };

  return (
    <div className="fixed bottom-24 right-5 z-50 mb-5 w-full max-w-sm">
      <div className="bg-white rounded-lg shadow-xl flex flex-col h-[60vh]">
        {/* Header */}
        <div className="bg-y-gold text-white p-4 rounded-t-lg flex justify-between items-center">
          <h3 className="text-lg font-semibold">비어 소믈리에 🍻</h3>
          <button
            onClick={onClose}
            className="text-white hover:text-gray-200 focus:outline-none"
            aria-label="Close chatbot"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>

        {/* Message Display Area */}
        <div className="flex-1 p-4 overflow-y-auto">
          {chatHistory.map((msg, index) => (
            <div
              key={index}
              className={`mb-4 flex ${
                msg.sender === 'user' ? 'justify-end' : ''
              }`}
            >
              <div
                className={`${
                  msg.sender === 'user'
                    ? 'bg-y-gold text-white'
                    : 'bg-gray-100 text-gray-800'
                } p-3 rounded-lg inline-block max-w-xs`}
              >
                <p className="text-sm" style={{ whiteSpace: 'pre-wrap' }}>{msg.text}</p>
              </div>
            </div>
          ))}
          {mutation.isPending && (
             <div className="mb-4">
               <div className="bg-gray-100 p-3 rounded-lg inline-block max-w-xs">
                 <p className="text-sm text-gray-500">답변을 생각하고 있어요...</p>
               </div>
             </div>
          )}
        </div>

        {/* Input Area */}
        <div className="p-4 border-t border-gray-200">
          <form
            className="flex items-center"
            onSubmit={(e) => {
              e.preventDefault();
              handleSendMessage();
            }}
          >
            <input
              type="text"
              placeholder="메시지를 입력하세요..."
              className="flex-1 border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-y-gold"
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              disabled={mutation.isPending}
            />
            <button
              type="submit"
              className="ml-3 bg-y-gold text-white px-4 py-2 rounded-lg hover:bg-y-brown focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-y-gold disabled:bg-gray-400"
              disabled={mutation.isPending}
            >
              전송
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ChatbotModal;

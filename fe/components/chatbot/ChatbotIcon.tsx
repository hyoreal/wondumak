import { IoChatbox } from 'react-icons/io5';

interface ChatbotIconProps {
  onClick: () => void;
}

const ChatbotIcon = ({ onClick }: ChatbotIconProps) => {
  return (
    <button
      onClick={onClick}
      className="fixed bottom-24 right-5 z-50 p-3 bg-y-gold rounded-full shadow-lg hover:bg-y-brown focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-y-gold transition-colors"
      aria-label="Open chatbot"
    >
      <IoChatbox className="w-8 h-8 text-white" />
    </button>
  );
};

export default ChatbotIcon;

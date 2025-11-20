import React, { useState } from 'react';

const Chatbot = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<{ text: string; isBot: boolean; coffeeIds?: number[] }[]>([
    { text: 'Hello! I can recommend coffees for you based on your preferences.', isBot: true }
  ]);
  const [loading, setLoading] = useState(false);

  const getRecommendation = async () => {
    setLoading(true);
    // Add user message
    setMessages(prev => [...prev, { text: 'Recommend me a coffee!', isBot: false }]);

    try {
      const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/ai/recommend`);
      const data = await res.json();

      setMessages(prev => [...prev, {
        text: data.recommendationText,
        isBot: true,
        coffeeIds: data.recommendedCoffeeIds
      }]);
    } catch (error) {
      setMessages(prev => [...prev, { text: 'Sorry, I could not get a recommendation right now.', isBot: true }]);
    }
    setLoading(false);
  };

  return (
    <div className="fixed bottom-4 right-4 z-50">
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="bg-y-brown text-white p-4 rounded-full shadow-lg hover:bg-y-gold transition-colors"
        >
          AI Assistant
        </button>
      )}

      {isOpen && (
        <div className="bg-white w-80 h-96 rounded-lg shadow-xl flex flex-col border border-gray-200">
          <div className="bg-y-brown text-white p-3 rounded-t-lg flex justify-between items-center">
            <span className="font-bold">Coffee AI</span>
            <button onClick={() => setIsOpen(false)} className="text-white hover:text-gray-200">X</button>
          </div>

          <div className="flex-1 overflow-y-auto p-4 space-y-3">
            {messages.map((msg, idx) => (
              <div key={idx} className={`flex flex-col ${msg.isBot ? 'items-start' : 'items-end'}`}>
                <div className={`p-2 rounded-lg text-sm max-w-[80%] ${msg.isBot ? 'bg-gray-100 text-gray-800' : 'bg-y-brown text-white'}`}>
                  {msg.text}
                </div>
                {msg.coffeeIds && (
                  <div className="mt-1 flex flex-wrap gap-1">
                    {msg.coffeeIds.map(id => (
                      <a key={id} href={`/coffee/${id}`} className="text-xs text-blue-500 hover:underline bg-blue-50 px-1 rounded">
                        View Coffee #{id}
                      </a>
                    ))}
                  </div>
                )}
              </div>
            ))}
            {loading && <div className="text-xs text-gray-500">Thinking...</div>}
          </div>

          <div className="p-3 border-t">
            <button
              onClick={getRecommendation}
              disabled={loading}
              className="w-full bg-y-yellow text-black py-2 rounded hover:bg-yellow-400 text-sm font-medium"
            >
              Get Recommendation
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Chatbot;

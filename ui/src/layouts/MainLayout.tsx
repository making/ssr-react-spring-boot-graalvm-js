import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen } from 'lucide-react';

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-white shadow-sm">
        <div className="container py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2 text-primary-700 hover:text-primary-600 transition-colors">
            <BookOpen size={28} className="text-primary-700" />
            <span className="text-xl font-semibold">Modern Blog</span>
          </Link>
          <nav className="flex gap-4">
            <Link 
              to="/" 
              className="text-gray-600 hover:text-primary-700 transition-colors font-medium"
            >
              Posts
            </Link>
          </nav>
        </div>
      </header>
      
      <main className="flex-grow container py-8">
        {children}
      </main>
      
      <footer className="bg-gray-100 border-t border-gray-200 py-6">
        <div className="container text-center text-gray-500 text-sm">
          <p>© {new Date().getFullYear()} Modern Blog. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;

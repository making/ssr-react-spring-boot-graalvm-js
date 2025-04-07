import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen } from 'lucide-react';
import ThemeToggle from '../components/ThemeToggle';

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-white dark:bg-gray-800 shadow-sm dark:shadow-gray-900/30">
        <div className="container py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2 text-primary-700 dark:text-primary-400 hover:text-primary-600 dark:hover:text-primary-300 transition-colors">
            <BookOpen size={28} className="text-primary-700 dark:text-primary-400" />
            <span className="text-xl font-semibold">Modern Blog</span>
          </Link>
          <div className="flex items-center gap-4">
            <nav className="flex gap-4">
              <Link 
                to="/" 
                className="text-gray-600 dark:text-gray-300 hover:text-primary-700 dark:hover:text-primary-400 transition-colors font-medium"
              >
                Posts
              </Link>
            </nav>
            <ThemeToggle />
          </div>
        </div>
      </header>
      
      <main className="flex-grow container py-8">
        {children}
      </main>
      
      <footer className="bg-gray-100 dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 py-6">
        <div className="container text-center text-gray-500 dark:text-gray-400 text-sm">
          <p>© {new Date().getFullYear()} Modern Blog. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;

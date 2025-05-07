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
      <header className="bg-bg-primary shadow-xs">
        <div className="container py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2 text-primary-700 hover:text-primary-600 transition-colors">
            <BookOpen size={28} className="text-primary-700" />
            <span className="text-xl font-semibold">Demo Blog</span>
          </Link>
          <div className="flex items-center gap-4">
            <nav className="flex gap-4">
              <Link 
                to="/" 
                className="text-text-secondary hover:text-primary-700 transition-colors font-medium"
              >
                Posts
              </Link>
            </nav>
            <ThemeToggle />
          </div>
        </div>
      </header>
      
      <main className="grow container py-8">
        {children}
      </main>
      
      <footer className="bg-bg-accent border-t border-border-primary py-6">
        <div className="container text-center text-text-muted text-sm">
          <p>© {new Date().getFullYear()} Demo Blog. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;

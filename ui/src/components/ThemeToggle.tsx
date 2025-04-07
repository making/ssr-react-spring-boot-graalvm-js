import React, { useEffect, useState } from 'react';
import { Moon, Sun } from 'lucide-react';
import { useTheme } from '../contexts/ThemeContext';

// Simple theme toggle component
const ThemeToggle: React.FC = () => {
  const [mounted, setMounted] = useState(false);
  const { theme, toggleTheme } = useTheme();

  // After mounting, we can show the toggle with the correct initial state
  useEffect(() => {
    setMounted(true);
  }, []);

  // For SSR and initial hydration - render a placeholder button
  // to avoid hydration mismatch
  if (!mounted) {
    return (
      <button 
        className="p-2 rounded-md text-text-secondary hover:text-primary-600 transition-colors"
        aria-label="Loading theme toggle"
      >
        <div className="w-5 h-5"></div>
      </button>
    );
  }

  return (
    <button
      onClick={toggleTheme}
      className="p-2 rounded-md text-text-secondary hover:text-primary-600 transition-colors"
      aria-label={theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}
    >
      {theme === 'dark' ? (
        <Sun size={20} />
      ) : (
        <Moon size={20} />
      )}
    </button>
  );
};

export default ThemeToggle;

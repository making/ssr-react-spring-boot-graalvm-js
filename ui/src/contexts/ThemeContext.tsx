import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';

// Type definitions in a separate file would be better for Fast Refresh,
// but keeping it here for simplicity
export type Theme = 'light' | 'dark';

interface ThemeContextType {
  theme: Theme;
  setTheme: (theme: Theme) => void;
  toggleTheme: () => void;
}

// Create context with default values
const ThemeContext = createContext<ThemeContextType>({
  theme: 'light',
  setTheme: () => {},
  toggleTheme: () => {}
});

interface ThemeProviderProps {
  children: ReactNode;
}

// Check if we're running in a browser environment
const isBrowser = typeof window !== 'undefined';

/**
 * Provider component for theme context
 */
export const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
  // Use state to track the current theme
  const [theme, setTheme] = useState<Theme>('light');
  // Track if the component has mounted (for SSR)
  const [mounted, setMounted] = useState(false);

  // Function to toggle between light and dark themes
  const toggleTheme = () => {
    setTheme(prevTheme => (prevTheme === 'light' ? 'dark' : 'light'));
  };

  // Effect to handle component mounting (client-side only)
  useEffect(() => {
    setMounted(true);
  }, []);

  // Effect to handle theme initialization from localStorage or system preference
  useEffect(() => {
    if (isBrowser) {
      // Check for stored theme preference in localStorage
      try {
        const storedTheme = localStorage.getItem('theme') as Theme | null;
        
        // Check for system preference if no stored preference
        const systemPrefersDark = window.matchMedia && 
          window.matchMedia('(prefers-color-scheme: dark)').matches;
        
        // Set theme based on stored preference or system preference
        const initialTheme = storedTheme || (systemPrefersDark ? 'dark' : 'light');
        setTheme(initialTheme);
        
        // Apply theme to document element
        if (initialTheme === 'dark') {
          document.documentElement.classList.add('dark');
        } else {
          document.documentElement.classList.remove('dark');
        }
      } catch (error) {
        // Fallback if localStorage is not available
        console.error('Error accessing localStorage:', error);
      }
    }
  }, []);

  // Effect to update DOM and localStorage when theme changes (client-side only)
  useEffect(() => {
    if (isBrowser && mounted) {
      try {
        // Update localStorage
        localStorage.setItem('theme', theme);
        
        // Update DOM
        if (theme === 'dark') {
          document.documentElement.classList.add('dark');
        } else {
          document.documentElement.classList.remove('dark');
        }
      } catch (error) {
        console.error('Error accessing localStorage:', error);
      }
    }
  }, [theme, mounted]);

  // Create the context value
  const contextValue = {
    theme,
    setTheme,
    toggleTheme
  };

  // Provide the context
  return (
    <ThemeContext.Provider value={contextValue}>
      {children}
    </ThemeContext.Provider>
  );
};

/**
 * Custom hook to use the theme context
 */
// eslint-disable-next-line react-refresh/only-export-components
export const useTheme = (): ThemeContextType => {
  return useContext(ThemeContext);
};


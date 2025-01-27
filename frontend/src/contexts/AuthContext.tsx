'use client';

import { useRouter } from 'next/navigation';
import type React from 'react';
import { createContext, useContext, useEffect, useState } from 'react';

import { PATHS } from '@/constants/paths';

type User = {
  id: string;
};

type AuthContextType = {
  user: User | null;
  requiresSetup: boolean;
  login: (id: string, password: string) => Promise<void>;
  logout: () => void;
  updateCredentials: (
    updatedId: string,
    updatedPassword: string,
  ) => Promise<void>;
  setRequiresSetup: (isUpdating: boolean) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// TODO: update state & cookie & routing management corresponding to BE apis
export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null);
  const [requiresSetup, setRequiresSetup] = useState(false);
  const router = useRouter();

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = async (id: string, password: string) => {
    console.log('🚀  id, password:', id, password);

    // TODO: Request to BE to validate credentials
    const tempRandomId = `user-${Date.now()}`;
    localStorage.setItem(
      'user',
      JSON.stringify({
        id: tempRandomId,
      }),
    );
    setUser({ id: tempRandomId });
  };

  const logout = () => {
    setUser(null);
    setRequiresSetup(false);
    localStorage.removeItem('user');
  };

  const updateCredentials = async (
    updatedId: string,
    updatedPassword: string,
  ) => {
    console.log('🚀  updatedId, updatedPassword:', updatedId, updatedPassword);

    // TODO: Request to BE to update credentials
    setRequiresSetup(false);
    const tempRandomId = `user-${Date.now()}`;
    localStorage.setItem(
      'user',
      JSON.stringify({
        id: tempRandomId,
      }),
    );
    setUser({ id: tempRandomId });
  };

  useEffect(() => {
    if (requiresSetup) router.replace(PATHS.SETUP);
    if (!user) router.replace(PATHS.LOGIN);
  }, [requiresSetup, router, user]);

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        updateCredentials,
        requiresSetup,
        setRequiresSetup,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

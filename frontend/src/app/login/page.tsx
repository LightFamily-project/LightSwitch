'use client';

import { zodResolver } from '@hookform/resolvers/zod';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { defaultUserNameAndPassword } from '@/constants/credentials';
import { PATHS } from '@/constants/paths';
import { useAuth } from '@/contexts/AuthContext';

const loginSchema = z.object({
  id: z.string().nonempty({ message: 'ID is required' }),
  password: z.string().nonempty({ message: 'Password is required' }),
});
type LoginSchemaType = z.infer<typeof loginSchema>;

const LoginPage = () => {
  const { setRequiresSetup, login } = useAuth();
  const router = useRouter();
  const form = useForm<LoginSchemaType>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      id: '',
      password: '',
    },
  });

  const submit = async (data: LoginSchemaType) => {
    if (
      data.id === defaultUserNameAndPassword &&
      data.password === defaultUserNameAndPassword
    ) {
      router.replace(PATHS.SETUP);
      setRequiresSetup(true);
    } else {
      try {
        await login(data.id, data.password);
        router.replace(PATHS.FEATURE_FLAG_MANAGEMENT);
      } catch {
        form.setError('root', {
          type: 'manual',
          message: 'Invalid ID or password',
        });
      }
    }
  };

  return (
    <Card className="mx-4 w-full max-w-md">
      <CardHeader className="space-y-2 pb-8">
        <CardTitle className="text-center text-2xl font-bold">
          Login to LightSwitch
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(submit)} className="space-y-5">
            <FormField
              control={form.control}
              name="id"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>User ID</FormLabel>
                  <FormControl>
                    <Input placeholder="Please enter your user ID" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Please enter your password"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {form.formState.errors.root && (
              <FormMessage>{form.formState.errors.root.message}</FormMessage>
            )}

            <Button type="submit" className="w-full">
              Login
            </Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
};

export default LoginPage;

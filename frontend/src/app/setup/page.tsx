'use client';

import { useRouter } from 'next/navigation';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { useAuth } from '@/contexts/AuthContext';
import { PATHS } from '@/constants/paths';

const setupSchema = z
  .object({
    newId: z.string().min(3, 'User ID must be at least 3 characters long'),
    newPassword: z
      .string()
      .min(8, 'Password must be at least 8 characters long'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'],
  });
type SetupSchemaType = z.infer<typeof setupSchema>;

const SetupPage = () => {
  const { updateCredentials } = useAuth();
  const router = useRouter();
  const form = useForm<SetupSchemaType>({
    resolver: zodResolver(setupSchema),
    defaultValues: {
      newId: '',
      newPassword: '',
      confirmPassword: '',
    },
  });

  const onSubmit = async (data: SetupSchemaType) => {
    try {
      await updateCredentials(data.newId, data.newPassword);
      router.push(PATHS.FEATURE_FLAG_MANAGEMENT);
    } catch {
      form.setError('root', {
        type: 'manual',
        message: 'Failed to update credentials. Please try again.',
      });
    }
  };

  return (
    <Card className="mx-auto w-full max-w-md">
      <CardHeader className="space-y-1">
        <CardTitle className="text-center text-2xl font-bold">
          Setup New Credentials
        </CardTitle>
        <CardDescription className="text-center">
          Please change your default credentials to continue
        </CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="newId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>New User ID</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter new user ID" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="newPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>New Password</FormLabel>
                  <FormControl>
                    <Input
                      type="password"
                      placeholder="Enter new password"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="confirmPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirm Password</FormLabel>
                  <FormControl>
                    <Input
                      type="password"
                      placeholder="Confirm new password"
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
              Set New Credentials
            </Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
};

export default SetupPage;

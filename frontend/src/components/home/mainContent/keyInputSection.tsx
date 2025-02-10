import { useContext } from 'react';

import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function KeyInputSection() {
  const context = useContext<homeContextType | null>(HomeContext);

  const { form } = context as homeContextType;

  return (
    <FormField
      control={form.control}
      name="key"
      render={() => (
        <FormItem>
          <FormLabel>Key</FormLabel>
          <FormControl>
            <Input
              className="flex w-[270px] items-center py-4"
              placeholder="Write your key name"
              onChange={(e) => form.setValue('key', e.target.value)}
              defaultValue={form.getValues('key')}
            />
          </FormControl>
        </FormItem>
      )}
    />
  );
}

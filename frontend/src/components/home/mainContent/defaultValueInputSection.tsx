import { useContext } from 'react';

import { Input } from '@/components/ui/input';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function DefaultValueInputSection() {
  const context = useContext<homeContextType | null>(HomeContext);
  const { form } = context as homeContextType;
  return (
    <>
      <span>Default Value</span>
      <Input
        className="flex w-[270px] items-center py-4"
        placeholder="Default Value"
        onChange={(e) => form.setValue('defaultValue', e.target.value)}
      />
    </>
  );
}

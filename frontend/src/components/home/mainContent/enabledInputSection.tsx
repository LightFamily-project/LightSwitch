import { useContext } from 'react';

import { Switch } from '@/components/ui/switch';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function EnabledInputSection() {
  const context = useContext<homeContextType | null>(HomeContext);
  const { form } = context as homeContextType;
  return (
    <div className="flex h-auto w-[270px] flex-row justify-between rounded-[0.2rem] border p-4">
      <span>enabled</span>
      <Switch
        onCheckedChange={(checked) => {
          form.setValue('enabled', checked);
        }}
      />
    </div>
  );
}

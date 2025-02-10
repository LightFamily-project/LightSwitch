import { useContext } from 'react';

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { homeContextType, HomeContext } from '@/contexts/HomeContext';

export default function SelectSection() {
  const context = useContext<homeContextType | null>(HomeContext);

  const { form } = context as homeContextType;

  return (
    <>
      <span>Type</span>
      <div className="w-[270px]">
        <Select
          onValueChange={(value) => {
            form.setValue('type', value);
          }}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Type" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="boolean">Boolean</SelectItem>
            <SelectItem value="string">String</SelectItem>
            <SelectItem value="number">Number</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </>
  );
}

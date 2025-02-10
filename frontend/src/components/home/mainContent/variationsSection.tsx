import { Trash2, PlusCircle } from 'lucide-react';
import { useContext } from 'react';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function VariationsSection() {
  const context = useContext<homeContextType | null>(HomeContext);
  const { removeVariation, addVariation, variationsState, handleVariations } =
    context as homeContextType;
  return (
    <div>
      <h3 className="mb-2 text-lg font-semibold">Variations</h3>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Key</TableHead>
            <TableHead>Value</TableHead>
            <TableHead className="w-[100px]">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {variationsState.map((variation, index) => {
            return (
              <TableRow key={index}>
                <TableCell>
                  <Input
                    value={variation.key}
                    onChange={(e) => {
                      handleVariations(e, 'key', index);
                    }}
                  />
                </TableCell>
                <TableCell>
                  <Input
                    value={variation.value.toString()}
                    onChange={(e) => {
                      handleVariations(e, 'value', index);
                    }}
                  />
                </TableCell>
                <TableCell>
                  <Button
                    type="button"
                    variant="ghost"
                    size="sm"
                    onClick={() => removeVariation(index)}
                  >
                    <Trash2 className="size-4" />
                  </Button>
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>

      <Button
        type="button"
        variant="outline"
        size="sm"
        onClick={addVariation}
        className="my-4"
      >
        <PlusCircle className="mr-2 size-4" />
        Add Variation
      </Button>
    </div>
  );
}

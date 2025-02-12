'use client';

import { PlusCircle, Trash2 } from 'lucide-react';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { FeatureFlag } from '@/data/mock-data';

interface VariationsTableProps {
  featureFlag: FeatureFlag;
  isEditing: boolean;
  setFeatureFlag: (flag: FeatureFlag) => void;
}

export function VariationsTable({
  featureFlag,
  isEditing,
  setFeatureFlag,
}: VariationsTableProps) {
  // Variations Change
  const handleVariationChange = (
    index: number,
    key: keyof FeatureFlag['variations'][0],
    value: string,
  ) => {
    const newVariations = [...featureFlag.variations];
    newVariations[index] = { ...newVariations[index], [key]: value };
    setFeatureFlag({ ...featureFlag, variations: newVariations });
  };

  // Variation Add
  const handleAddVariation = () => {
    const newVariation = {
      key: '',
      value:
        featureFlag.type === 'Bool'
          ? false
          : featureFlag.type === 'Number'
            ? 0
            : '',
    };
    setFeatureFlag({
      ...featureFlag,
      variations: [...featureFlag.variations, newVariation],
    });
  };

  // Variation Delete
  const handleDeleteVariation = (index: number) => {
    const newVariations = featureFlag.variations.filter((_, i) => i !== index);
    setFeatureFlag({ ...featureFlag, variations: newVariations });
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-xl">Variations</CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Key</TableHead>
              <TableHead>Value</TableHead>
              {isEditing && (
                <TableHead className="w-[100px]">Actions</TableHead>
              )}
            </TableRow>
          </TableHeader>
          <TableBody>
            {featureFlag.variations.map((variation, index) => (
              <TableRow key={index}>
                <TableCell>
                  <Input
                    value={variation.key}
                    readOnly={!isEditing}
                    onChange={(e) =>
                      handleVariationChange(index, 'key', e.target.value)
                    }
                  />
                </TableCell>
                <TableCell>
                  <Input
                    value={variation.value.toString()}
                    readOnly={!isEditing}
                    onChange={(e) =>
                      handleVariationChange(index, 'value', e.target.value)
                    }
                  />
                </TableCell>
                {isEditing && (
                  <TableCell>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDeleteVariation(index)}
                    >
                      <Trash2 className="size-4" />
                    </Button>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>

        {/* Add Variation Btn */}
        {isEditing && (
          <Button
            variant="outline"
            size="sm"
            onClick={handleAddVariation}
            className="mt-4"
          >
            <PlusCircle className="mr-2 size-4" />
            Add Variation
          </Button>
        )}
      </CardContent>
    </Card>
  );
}

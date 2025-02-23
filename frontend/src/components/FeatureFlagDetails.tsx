'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { FeatureFlag } from '@/types/featureFlag';

type FeatureFlagDetailsProps = {
  featureFlag: FeatureFlag;
  setFeatureFlag: (flag: FeatureFlag) => void;
  isEditing: boolean;
};

export function FeatureFlagDetails({
  featureFlag,
  setFeatureFlag,
  isEditing,
}: FeatureFlagDetailsProps) {
  return (
    <Card className="space-y-1">
      <CardHeader>
        <CardTitle className="text-xl">Feature Flag Details</CardTitle>
      </CardHeader>
      <CardContent className="grid grid-cols-1 gap-5 md:grid-cols-2">
        <div>
          <Label htmlFor="key">Key</Label>
          <Input
            id="key"
            value={featureFlag.key}
            readOnly={!isEditing}
            onChange={(e) =>
              setFeatureFlag({ ...featureFlag, key: e.target.value })
            }
          />
        </div>
        <div>
          <Label htmlFor="type">Type</Label>
          {isEditing ? (
            <Select
              defaultValue={featureFlag.type}
              onValueChange={(value) =>
                setFeatureFlag({
                  ...featureFlag,
                  type: value as FeatureFlag['type'],
                })
              }
            >
              <SelectTrigger>
                <SelectValue placeholder="Select type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="Bool">Boolean</SelectItem>
                <SelectItem value="Number">Number</SelectItem>
                <SelectItem value="String">String</SelectItem>
              </SelectContent>
            </Select>
          ) : (
            <Input id="type" value={featureFlag.type} readOnly />
          )}
        </div>
        <div>
          <Label htmlFor="createdAt">Created At</Label>
          <Input id="createdAt" value={featureFlag.createdAt} readOnly />
        </div>
        <div>
          <Label htmlFor="createdBy">Created By</Label>
          <Input id="createdBy" value={featureFlag.createdBy} readOnly />
        </div>
        <div className="flex items-center space-x-2">
          <Label htmlFor="status">Status</Label>
          <Switch
            id="status"
            checked={featureFlag.enabled}
            disabled={!isEditing}
            onCheckedChange={(checked) =>
              setFeatureFlag({ ...featureFlag, enabled: checked })
            }
          />
        </div>
      </CardContent>
    </Card>
  );
}

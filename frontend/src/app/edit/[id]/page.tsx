'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';

import { Button } from '@/components/ui/button';
import { FeatureFlagDetails } from '@/components/FeatureFlagDetails';
import { VariationsTable } from '@/components/VariationsTable';
import { mockFeatureFlags } from '@/data/mock-data';
import { FeatureFlag } from '@/types/featureFlag';

export default function FeatureFlagSettings() {
  const params = useParams();
  const router = useRouter();
  const id = params.id as string;

  const targetFeatureFlag =
    mockFeatureFlags.find((flag) => flag.id === id) ?? null;
  const [featureFlag, setFeatureFlag] = useState<FeatureFlag | null>(
    targetFeatureFlag ?? null,
  );
  const [isEditing, setIsEditing] = useState(false);

  if (!featureFlag) {
    return <div>Feature flag not found</div>;
  }

  const handleSave = () => {
    console.log('Saved Feature Flag:', featureFlag);
    setIsEditing(false);
  };

  const handleCancel = () => {
    setFeatureFlag(targetFeatureFlag);
    setIsEditing(false);
  };

  const handleDelete = () => {
    console.log('Deleting feature flag:', featureFlag);
    router.push('/');
  };

  return (
    <div className="space-y-5 p-8">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">Feature Flag Settings</h2>
        <div className="space-x-5">
          {isEditing ? (
            <>
              <Button onClick={handleSave}>Save</Button>
              <Button variant="outline" onClick={handleCancel}>
                Cancel
              </Button>
            </>
          ) : (
            <Button onClick={() => setIsEditing(true)}>Edit</Button>
          )}
        </div>
      </div>

      <FeatureFlagDetails
        featureFlag={featureFlag}
        setFeatureFlag={setFeatureFlag}
        isEditing={isEditing}
      />

      <VariationsTable
        featureFlag={featureFlag}
        setFeatureFlag={setFeatureFlag}
        isEditing={isEditing}
      />

      {isEditing && (
        <div className="flex justify-end">
          <Button variant="destructive" onClick={handleDelete}>
            Delete Feature Flag
          </Button>
        </div>
      )}
    </div>
  );
}

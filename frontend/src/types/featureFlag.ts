export type Variation = {
  key: string;
  value: boolean | number | string;
};

export type FeatureFlag = {
  id: string;
  key: string;
  createdAt: string;
  createdBy: string;
  enabled: boolean;
  type: 'Bool' | 'Number' | 'String';
  variations: Variation[];
  defaultValue: boolean | number | string;
};

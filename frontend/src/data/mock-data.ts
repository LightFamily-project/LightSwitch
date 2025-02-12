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

export const mockFeatureFlags: FeatureFlag[] = [
  {
    id: '1',
    key: 'new-dashboard',
    createdAt: '2024-01-15',
    createdBy: 'Alice',
    enabled: true,
    type: 'Bool',
    variations: [
      { key: 'on', value: true },
      { key: 'off', value: false },
    ],
    defaultValue: false,
  },
  {
    id: '2',
    key: 'dark-mode',
    createdAt: '2024-01-16',
    createdBy: 'Bob',
    enabled: false,
    type: 'Bool',
    variations: [
      { key: 'enabled', value: true },
      { key: 'disabled', value: false },
    ],
    defaultValue: false,
  },
  {
    id: '3',
    key: 'user-limit',
    createdAt: '2024-01-17',
    createdBy: 'Charlie',
    enabled: true,
    type: 'Number',
    variations: [
      { key: 'free', value: 10 },
      { key: 'pro', value: 100 },
      { key: 'enterprise', value: 1000 },
    ],
    defaultValue: 10,
  },
];

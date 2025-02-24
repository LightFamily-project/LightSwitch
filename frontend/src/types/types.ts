export enum FeatureFlagType {
  Boolean = 'Boolean',
  String = 'String',
  Number = 'Number',
}

export interface FeatureFlagTableType {
  Key: string;
  type: FeatureFlagType;
  createdAt: string;
  createdBy: string;
  status: boolean;
}

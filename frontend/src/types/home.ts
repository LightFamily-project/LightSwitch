export interface TableDataType {
  Name: string;
  Type: string;
  Created: string;
  By: string;
  Status: boolean;
  'Default Value': boolean | string | number;
}

export interface CreateFlagType {
  key: string;
  type: string;
  enabled: false;
  DefaultValue: string | boolean | number;
  variations: [
    {
      key: string;
      value: string | boolean | number;
    },
  ];
}

export interface Variation {
  key: string;
  value: string | number | boolean;
}

export interface FieldArrayType {
  key: string;
  type: string;
  enabled: boolean;
  defaultValue: string | number | boolean;
  variations: Variation[]; // Variation 배열 사용
}

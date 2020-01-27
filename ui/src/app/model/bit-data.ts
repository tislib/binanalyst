export type BitType = 'var' | 'operational' | 'const';
export type BinaryValue = 'TRUE' | 'FALSE' | 'UNSET';
export type BitOperation = 'AND' | 'OR' | 'XOR' | 'NOT';

export interface BitData {
  type: BitType;
  name: string;
  value: BinaryValue;
  operation: BitOperation;
  bits: string[];
}

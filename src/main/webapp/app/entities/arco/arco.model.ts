import { INodo } from 'app/entities/nodo/nodo.model';

export interface IArco {
  id?: number;
  weight?: number | null;
  from?: INodo | null;
  to?: INodo | null;
}

export class Arco implements IArco {
  constructor(public id?: number, public weight?: number | null, public from?: INodo | null, public to?: INodo | null) {}
}

export function getArcoIdentifier(arco: IArco): number | undefined {
  return arco.id;
}

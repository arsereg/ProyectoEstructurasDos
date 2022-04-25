import { INodo } from 'app/entities/nodo/nodo.model';

export interface IArco {
  id?: number;
  weight?: number | null;
  froms?: INodo[] | null;
  tos?: INodo[] | null;
}

export class Arco implements IArco {
  constructor(public id?: number, public weight?: number | null, public froms?: INodo[] | null, public tos?: INodo[] | null) {}
}

export function getArcoIdentifier(arco: IArco): number | undefined {
  return arco.id;
}

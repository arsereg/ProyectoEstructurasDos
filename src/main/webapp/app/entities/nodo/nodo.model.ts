import { IArco } from 'app/entities/arco/arco.model';

export interface INodo {
  id?: number;
  name?: string | null;
  x?: number | null;
  y?: number | null;
  incomings?: IArco[] | null;
  goings?: IArco[] | null;
}

export class Nodo implements INodo {
  constructor(
    public id?: number,
    public name?: string | null,
    public x?: number | null,
    public y?: number | null,
    public incomings?: IArco[] | null,
    public goings?: IArco[] | null
  ) {}
}

export function getNodoIdentifier(nodo: INodo): number | undefined {
  return nodo.id;
}

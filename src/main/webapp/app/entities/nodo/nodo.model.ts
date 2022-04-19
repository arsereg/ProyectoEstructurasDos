export interface INodo {
  id?: number;
  name?: string | null;
  x?: number | null;
  y?: number | null;
}

export class Nodo implements INodo {
  constructor(public id?: number, public name?: string | null, public x?: number | null, public y?: number | null) {}
}

export function getNodoIdentifier(nodo: INodo): number | undefined {
  return nodo.id;
}

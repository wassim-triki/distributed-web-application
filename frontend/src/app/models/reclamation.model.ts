export interface Reclamation {
    id?: number;
    dateReclamation?: string; // use string if binding from input or JSON
    description: string;
    orderId: number;
    statut?: string; // default: 'En attente'
    type: 'PRODUIT_ENDOMMAGE' | 'RETARD_LIVRAISON' | 'ERREUR_COMMANDE' | 'SERVICE_CLIENT' | 'AUTRE';
    emailClient: string;
  }
  
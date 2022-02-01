import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsolationsDetails } from '../isolations-details.model';
import { IsolationsDetailsService } from '../service/isolations-details.service';

@Component({
  templateUrl: './isolations-details-delete-dialog.component.html',
})
export class IsolationsDetailsDeleteDialogComponent {
  isolationsDetails?: IIsolationsDetails;

  constructor(protected isolationsDetailsService: IsolationsDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isolationsDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

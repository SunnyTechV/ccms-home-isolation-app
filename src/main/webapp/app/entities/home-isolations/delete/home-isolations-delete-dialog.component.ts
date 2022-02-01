import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHomeIsolations } from '../home-isolations.model';
import { HomeIsolationsService } from '../service/home-isolations.service';

@Component({
  templateUrl: './home-isolations-delete-dialog.component.html',
})
export class HomeIsolationsDeleteDialogComponent {
  homeIsolations?: IHomeIsolations;

  constructor(protected homeIsolationsService: HomeIsolationsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.homeIsolationsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

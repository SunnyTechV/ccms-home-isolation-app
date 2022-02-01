import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssessmentAnswers } from '../assessment-answers.model';
import { AssessmentAnswersService } from '../service/assessment-answers.service';

@Component({
  templateUrl: './assessment-answers-delete-dialog.component.html',
})
export class AssessmentAnswersDeleteDialogComponent {
  assessmentAnswers?: IAssessmentAnswers;

  constructor(protected assessmentAnswersService: AssessmentAnswersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assessmentAnswersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

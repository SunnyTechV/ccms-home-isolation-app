import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssessmentAnswersComponent } from './list/assessment-answers.component';
import { AssessmentAnswersDetailComponent } from './detail/assessment-answers-detail.component';
import { AssessmentAnswersUpdateComponent } from './update/assessment-answers-update.component';
import { AssessmentAnswersDeleteDialogComponent } from './delete/assessment-answers-delete-dialog.component';
import { AssessmentAnswersRoutingModule } from './route/assessment-answers-routing.module';

@NgModule({
  imports: [SharedModule, AssessmentAnswersRoutingModule],
  declarations: [
    AssessmentAnswersComponent,
    AssessmentAnswersDetailComponent,
    AssessmentAnswersUpdateComponent,
    AssessmentAnswersDeleteDialogComponent,
  ],
  entryComponents: [AssessmentAnswersDeleteDialogComponent],
})
export class AssessmentAnswersModule {}

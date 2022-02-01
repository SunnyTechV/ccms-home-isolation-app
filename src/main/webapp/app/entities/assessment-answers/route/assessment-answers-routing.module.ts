import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssessmentAnswersComponent } from '../list/assessment-answers.component';
import { AssessmentAnswersDetailComponent } from '../detail/assessment-answers-detail.component';
import { AssessmentAnswersUpdateComponent } from '../update/assessment-answers-update.component';
import { AssessmentAnswersRoutingResolveService } from './assessment-answers-routing-resolve.service';

const assessmentAnswersRoute: Routes = [
  {
    path: '',
    component: AssessmentAnswersComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssessmentAnswersDetailComponent,
    resolve: {
      assessmentAnswers: AssessmentAnswersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssessmentAnswersUpdateComponent,
    resolve: {
      assessmentAnswers: AssessmentAnswersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssessmentAnswersUpdateComponent,
    resolve: {
      assessmentAnswers: AssessmentAnswersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assessmentAnswersRoute)],
  exports: [RouterModule],
})
export class AssessmentAnswersRoutingModule {}

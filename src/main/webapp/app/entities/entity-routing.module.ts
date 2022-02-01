import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'home-isolations',
        data: { pageTitle: 'ccmsHomeIsolationApp.homeIsolations.home.title' },
        loadChildren: () => import('./home-isolations/home-isolations.module').then(m => m.HomeIsolationsModule),
      },
      {
        path: 'isolations-details',
        data: { pageTitle: 'ccmsHomeIsolationApp.isolationsDetails.home.title' },
        loadChildren: () => import('./isolations-details/isolations-details.module').then(m => m.IsolationsDetailsModule),
      },
      {
        path: 'assessment',
        data: { pageTitle: 'ccmsHomeIsolationApp.assessment.home.title' },
        loadChildren: () => import('./assessment/assessment.module').then(m => m.AssessmentModule),
      },
      {
        path: 'questions',
        data: { pageTitle: 'ccmsHomeIsolationApp.questions.home.title' },
        loadChildren: () => import('./questions/questions.module').then(m => m.QuestionsModule),
      },
      {
        path: 'questions-options',
        data: { pageTitle: 'ccmsHomeIsolationApp.questionsOptions.home.title' },
        loadChildren: () => import('./questions-options/questions-options.module').then(m => m.QuestionsOptionsModule),
      },
      {
        path: 'assessment-answers',
        data: { pageTitle: 'ccmsHomeIsolationApp.assessmentAnswers.home.title' },
        loadChildren: () => import('./assessment-answers/assessment-answers.module').then(m => m.AssessmentAnswersModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

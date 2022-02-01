import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssessmentAnswers } from '../assessment-answers.model';

@Component({
  selector: 'jhi-assessment-answers-detail',
  templateUrl: './assessment-answers-detail.component.html',
})
export class AssessmentAnswersDetailComponent implements OnInit {
  assessmentAnswers: IAssessmentAnswers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessmentAnswers }) => {
      this.assessmentAnswers = assessmentAnswers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

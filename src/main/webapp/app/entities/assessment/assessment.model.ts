import dayjs from 'dayjs/esm';
import { IAssessmentAnswers } from 'app/entities/assessment-answers/assessment-answers.model';
import { IHomeIsolations } from 'app/entities/home-isolations/home-isolations.model';

export interface IAssessment {
  id?: number;
  assessmentDate?: dayjs.Dayjs;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  assessmentAnswers?: IAssessmentAnswers[] | null;
  homeIsolations?: IHomeIsolations | null;
}

export class Assessment implements IAssessment {
  constructor(
    public id?: number,
    public assessmentDate?: dayjs.Dayjs,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public assessmentAnswers?: IAssessmentAnswers[] | null,
    public homeIsolations?: IHomeIsolations | null
  ) {}
}

export function getAssessmentIdentifier(assessment: IAssessment): number | undefined {
  return assessment.id;
}

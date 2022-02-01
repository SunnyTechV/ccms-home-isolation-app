import dayjs from 'dayjs/esm';
import { IQuestions } from 'app/entities/questions/questions.model';
import { IAssessment } from 'app/entities/assessment/assessment.model';

export interface IAssessmentAnswers {
  id?: number;
  answer?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  questions?: IQuestions | null;
  assessment?: IAssessment | null;
}

export class AssessmentAnswers implements IAssessmentAnswers {
  constructor(
    public id?: number,
    public answer?: string | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public questions?: IQuestions | null,
    public assessment?: IAssessment | null
  ) {}
}

export function getAssessmentAnswersIdentifier(assessmentAnswers: IAssessmentAnswers): number | undefined {
  return assessmentAnswers.id;
}

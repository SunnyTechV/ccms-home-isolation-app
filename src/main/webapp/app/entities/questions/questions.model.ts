import dayjs from 'dayjs/esm';
import { IQuestionsOptions } from 'app/entities/questions-options/questions-options.model';
import { IAssessmentAnswers } from 'app/entities/assessment-answers/assessment-answers.model';
import { QuestionType } from 'app/entities/enumerations/question-type.model';

export interface IQuestions {
  id?: number;
  question?: string | null;
  questionDesc?: string | null;
  questionType?: QuestionType | null;
  active?: boolean | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  questionsOptions?: IQuestionsOptions[] | null;
  assessmentAnswers?: IAssessmentAnswers | null;
}

export class Questions implements IQuestions {
  constructor(
    public id?: number,
    public question?: string | null,
    public questionDesc?: string | null,
    public questionType?: QuestionType | null,
    public active?: boolean | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public questionsOptions?: IQuestionsOptions[] | null,
    public assessmentAnswers?: IAssessmentAnswers | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getQuestionsIdentifier(questions: IQuestions): number | undefined {
  return questions.id;
}

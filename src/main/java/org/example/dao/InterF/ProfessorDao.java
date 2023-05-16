package org.example.dao.InterF;

import org.example.domain.Professor;
import org.example.domain.WebRegClass;

public interface ProfessorDao {
    Professor getProfessorById(int id);

    Professor getProfessorByClass(WebRegClass c);
}

#include "gradebook.h"
#include <string.h>

/*
 * Name: Waiyuk  
 */

struct Gradebook gradebook;

/**
 * Adds a new student to the gradebook and sets all the student's grades to 0.
 *
 * Updates assignment_averages and course_average based on the new grades.
 *
 * @param name The name of the student.
 * @param gtid The GTID of the student.
 * @param year The year of the student.
 * @param major The major of the student.
 * @return SUCCESS if the student is added, otherwise ERROR if the student can't
 * be added (duplicate name / GTID, no space in gradebook, invalid major).
 */
int add_student(char *name, int gtid, int year, char *major) {
  // TODO: Implement this function

  //check if pointer is NULL
  if (name == NULL || major == NULL) { 
    return ERROR;
  }

  //check if gradebook have space and length of name is under max length
  if (gradebook.size >= MAX_ENTRIES || strlen(name) >= MAX_NAME_LENGTH) {
    return ERROR;
  }

  //start from entrie 0 to the last entrie
  for (int i = 0; i < gradebook.size; i++) {
    //with in each entrie, check student gtid and name to see if any duplicate
    if (gradebook.entries[i].student.gtid == gtid || strcmp(gradebook.entries[i].student.name, name) == 0) {
      return ERROR;
    }
  }

  //create Major emum
  enum Major m;
  if (strcmp(major, "CS") == 0) {
    m = CS;
  } else if (strcmp(major, "CE") == 0) {
    m = CE;
  } else if (strcmp(major, "EE") == 0) {
    m = EE;
  } else if (strcmp(major, "IE") == 0) {
    m = IE;
  } else {
    return ERROR;
  }

  //if all check pass, create a pointer that point to the end of gradebook
  struct GradebookEntry *new_student = &gradebook.entries[gradebook.size];
  //save info into the student 
  strncpy(new_student->student.name, name, MAX_NAME_LENGTH);
  new_student->student.gtid = gtid;
  new_student->student.year = year;
  new_student->student.major = m;

  //update grade of assignment
  for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
        new_student->grades[i] = 0.0;
  }

  //update size
  gradebook.size++;

  //update average
  calculate_average(new_student);
  calculate_course_average();

  return SUCCESS;
}

/**
 * Updates the grade of a specific assignment for a student and updates that
 * student's average grade.
 * 
 * Ensure that the overall course averages are still up-to-date after these grade updates.
 *
 * @param name The name of the student.
 * @param assignmentType The type of assignment.
 * @param newGrade The new grade.
 * @return SUCCESS if the grade is updated, otherwise ERROR if the grade isn't (student not found).
 */
int update_grade(char *name, enum Assignment assignment_type, double new_grade) {
  // TODO: Implement this function

  //check if name is null
  if (name == NULL) {
    return ERROR;
  }

  //add grade into gradebook
  for (int i = 0; i < gradebook.size; i++) {
    //check if the name is equal
    if (strcmp(gradebook.entries[i].student.name, name) == 0) {
      //update grade
      gradebook.entries[i].grades[assignment_type] = new_grade;
      //update average
      calculate_average(&gradebook.entries[i]);
      calculate_course_average();

      return SUCCESS;
    }
  }

  return ERROR;
}

/**
 * Adds a new student to the gradebook and initializes each of the student's
 * grades with the grades passed in.
 *
 * Additionally, will update the overall assignment_averages and course_average
 * based on the new added student.
 *
 * @param name The name of the student.
 * @param gtid The GTID of the student.
 * @param year The year of the student.
 * @param major The major of the student.
 * @param grades An array of grades for the student.
 * @return SUCCESS if the student is added and the averages updated, otherwise ERROR if the student can't
 * be added (duplicate name / GTID, no space in gradebook, invalid major).
 */
int add_student_with_grades(char *name, int gtid, int year, char *major,
                            double *grades) {
  // TODO: Implement this function

  //check if pointer is NULL
  if (name == NULL || major == NULL) {
    return ERROR;
  }
  //check if gradebook have space and length of name is under max length
  if (gradebook.size >= MAX_ENTRIES || strlen(name) >= MAX_NAME_LENGTH) {
    return ERROR;
  }
  //start from entrie 0 to the last entrie
  for (int i = 0; i < gradebook.size; i++) {
    //with in each entrie, check student gtid and name to see if any duplicate
    if (gradebook.entries[i].student.gtid == gtid || strcmp(gradebook.entries[i].student.name, name) == 0) {
      return ERROR;
    }
  }

  //create Major emum
  enum Major m;
  if (strcmp(major, "CS") == 0) {
    m = CS;
  } else if (strcmp(major, "CE") == 0) {
    m = CE;
  } else if (strcmp(major, "EE") == 0) {
    m = EE;
  } else if (strcmp(major, "IE") == 0) {
    m = IE;
  } else {
    return ERROR;
  }

  //if all check pass, create a pointer that point to the end of gradebook
  struct GradebookEntry *new_student = &gradebook.entries[gradebook.size];
  //save info into the student 
  strncpy(new_student->student.name, name, MAX_NAME_LENGTH);
  new_student->student.gtid = gtid;
  new_student->student.year = year;
  new_student->student.major = m;

  //update grade of assignment
  for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
        new_student->grades[i] = *(grades + i);
  }

  //update size
  gradebook.size++;

  //update average
  calculate_average(new_student);
  calculate_course_average();

  return SUCCESS;
}

/**
 * Calculates the average grade for a specific gradebook entry and updates the
 * struct as appropriate.
 *
 * @param entry The gradebook entry for which to recalculate the average.
 * @return SUCCESS if the average is updated, ERROR if pointer is NULL
 */
int calculate_average(struct GradebookEntry *entry) {

  //check if pointer is NULL
    if (entry == NULL) {
        return ERROR;
    }

    double total = 0.0;
    //calculate total grade
    for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
        total += entry->grades[i];
    }
    //calculate average
    entry->average = total / NUM_ASSIGNMENTS;
    return SUCCESS;
}
/**
 * Calculates and update the overall course average and assignment averages. 
 * The average should be calculated by taking the averages of the student's 
 * averages, NOT the assignment averages.
 *
 * If the gradebook is empty, set the course and assignment averages to 0
 * and return ERROR.
 * 
 * @return SUCCESS if the averages are calculated properly, ERROR if gradebook
 * is empty
 */
int calculate_course_average(void) {
    //check if no student in gradebook
    if (gradebook.size == 0) {
        gradebook.course_average = 0;
        for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
            gradebook.assignment_averages[i] = 0;
        }
        return ERROR;
    }

    double totalCourse = 0.0;
    double totalAssignments[NUM_ASSIGNMENTS] = {0};
    //calculate total grade
    for (int i = 0; i < gradebook.size; i++) {
        calculate_average(&gradebook.entries[i]);
        totalCourse += gradebook.entries[i].average;

        for (int j = 0; j < NUM_ASSIGNMENTS; j++) {
            totalAssignments[j] += gradebook.entries[i].grades[j];
        }
    }
    //calcualte overall average
    gradebook.course_average = totalCourse / gradebook.size;
    for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
        gradebook.assignment_averages[i] = totalAssignments[i] / gradebook.size;
    }

    return SUCCESS;
}

/**
 * Searches for a student in the gradebook by name.
 *
 * @param name The name of the student.
 * @return The index of the student in the gradebook, or ERROR if not found.
 */
int search_student(char *name) {
  // TODO: Implement this function

  //check if pointer is NULL
  if (name == NULL) {
    return ERROR;
  }

  for (int i = 0; i < gradebook.size; i++) {
    //check if name is equal
    if (strcmp(gradebook.entries[i].student.name, name) == 0) {
      return i;
    }
  }

  return ERROR;
}

/**
 * Remove a student from the gradebook while maintaining the ordering of the gradebook.
 *
 * Additionally, update the overall assignment_averages and course_average
 * based on the removed student and decrement the size of gradebook.
 *
 * If the gradebook is empty afterwards, SUCCESS should still be returned and
 * averages should be set to 0.
 *
 * @param name The name of the student to be withdrawn.
 * @return SUCCESS if the student is successfully removed, otherwise ERROR if
 * the student isn't found.
 */
int withdraw_student(char *name) {
  // TODO: Implement this function
  
  //check if pointer is NULL
  if (name == NULL) {
    return ERROR;
  }

  for (int i = 0; i < gradebook.size; i++) {
    //check if the name is equal
    if (strcmp(gradebook.entries[i].student.name, name) == 0) {
      for (int j = i; j < gradebook.size - 1; j++) {
        //move student foward by one space
        gradebook.entries[j] = gradebook.entries[j + 1];
      }
      //decrease size
      gradebook.size--;
      //update average
      calculate_course_average();
      return SUCCESS;
    }
  }



  return ERROR;
}

/**
 * Populate the provided array with the GTIDs of the 5 students with the highest
 * grades. The GTIDs should be placed in descending order of averages. 
 * 
 * If unable to populate the full array (less than 5 students in gradebook), 
 * fill in the remaining values with INVALID_GTID.
 *
 * @param gtids An array to store the top five gtids.
 * @return SUCCESS if gtids are found, otherwise ERROR if gradebook is empty
 */
int top_five_gtid(int *gtids) {
  // TODO: Implement this function

  //check if no student in gradebook
  if (gradebook.size == 0) {
    return ERROR;
  }

  //sort by average
  for (int i = 0; i < gradebook.size - 1; i++) {
    int max = i;
    for (int j = i + 1; j < gradebook.size; j++) {
      //compare average
      if (gradebook.entries[j].average > gradebook.entries[max].average) {
        max = j;
      }
    }
    //switch
    struct GradebookEntry temp = gradebook.entries[i];
    gradebook.entries[i] = gradebook.entries[max];
    gradebook.entries[max] = temp;
  }

  //find top five student in gradebook
  for (int i = 0; i < 5; i++) {
    if (i < gradebook.size) {
      gtids[i] = gradebook.entries[i].student.gtid;
    } else {
      //if less than five in gradebook
      gtids[i] = INVALID_GTID;
    }
  }

    return SUCCESS;
}

/**
 * Sorts the gradebook entries by name in alphabetical order (First, Last).
 *
 * @return SUCCESS if names are sorted, ERROR is gradebook is empty.
 */
int sort_name(void) {
  // TODO: Implement this function

  //check if no student in gradebook
  if (gradebook.size == 0) {
    return ERROR;
  }

  for (int i = 0; i < gradebook.size - 1; i++) {
    int min = i;
    for (int j = i + 1; j < gradebook.size; j++) {
      //compare student name
      if (strcmp(gradebook.entries[j].student.name, gradebook.entries[min].student.name) < 0) {
        min = j;
      }
    }
    //switch student
    struct GradebookEntry temp = gradebook.entries[i];
    gradebook.entries[i] = gradebook.entries[min];
    gradebook.entries[min] = temp;
  }

    return SUCCESS;
}

/**
 * Sorts the gradebook entries by average grades in descending order.
 *
 * @return SUCCESS if entries are sorted, ERROR if gradebook is empty.
 */
int sort_averages(void) {
  // TODO: Implement this function

  //check if no student in gradebook
  if (gradebook.size == 0) {
    return ERROR;
  }

  for (int i = 0; i < gradebook.size - 1; i++) {
    int max = i;
    for (int j = i + 1; j < gradebook.size; j++) {
      //compare average
      if (gradebook.entries[j].average > gradebook.entries[max].average) {
        max = j;
      }
    }
    //switch student space
    struct GradebookEntry temp = gradebook.entries[i];
    gradebook.entries[i] = gradebook.entries[max];
    gradebook.entries[max] = temp;
  }

    return SUCCESS;
}

/**
 * Prints the entire gradebook in the format
 * student_name,major,grade1,grade2,...,student_average\n
 * 
 * Overall Averages:
 * grade1_average,grade2_average,...,course_average\n
 * 
 * Note 1: The '\n' shouldn’t print, just represents the newline for this example.
 * Note 2: There is an empty line above the “Overall Averages:” line.
 * 
 * All of the floats that you print must be manually rounded to 2 decimal places.
 *
 * @return SUCCESS if gradebook is printed, ERROR if gradebook is empty.
 */
int print_gradebook(void) {
  // TODO: Implement this function

  //check if no student in gradebook
  if (gradebook.size == 0) {
        return ERROR;
  }


  for (int i = 0; i < gradebook.size; i++) {
    char* major;
    switch (gradebook.entries[i].student.major) {
        case CS: major =  "CS"; break;
        case CE: major = "CE"; break;
        case EE: major = "EE"; break;
        case IE: major = "IE"; break;
        default: return ERROR;
    }
    printf("%s,%s,", gradebook.entries[i].student.name, major);

    for (int j = 0; j < NUM_ASSIGNMENTS; j++) {
      printf("%.2f,", gradebook.entries[i].grades[j]);
    }
    printf("%.2f\n", gradebook.entries[i].average);
  }

  printf("\n");

  printf("Overall Averages:\n");
  for (int i = 0; i < NUM_ASSIGNMENTS; i++) {
        printf("%.2f,", gradebook.assignment_averages[i]);
  }
  printf("%.2f\n", gradebook.course_average);


  return SUCCESS;
}

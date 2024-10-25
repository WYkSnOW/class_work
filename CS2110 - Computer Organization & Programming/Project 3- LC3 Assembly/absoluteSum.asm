;; ============================================================================
;; CS 2110 - Fall 2023
;; Project 3 - wordProcessor
;; ============================================================================
;; Name: Waiyuk Kwong
;; ============================================================================

;; =============================== absoluteSum ===============================
;; DESCRIPTION:
;; This function calculates the sum of the absolute values of an array of 
;; integers.
;; The starting address of the array and the length of the array are provided
;; in memory The answer should also be stored in memory at x3050 (ANSWER)

;; SUGGESTED PSUEDOCODE:
;; answer = 0
;; currNum = 0
;; i = 0
;; arrLength = ARR.length()
;; while (arrLength > 0)
;;    currNum = ARR[i]
;;    if (currNum < 0):
;;        currNum = -(currNum); 
;;    answer += currNum;
;;    i++
;;    arrLength--
;; return

.orig x3000
;; YOUR CODE HERE!
    AND R0, R0, #0 ;; R0 = answer = 0
    AND R1, R1, #0 ;; R1 = currNum = 0
    

    LD R3, LEN ;; R3 = ARR.length() = LEN = 5
    
    LD R4, ARR ;; R4 = address of ARR[0]
    
WHILE
    ADD R3, R3, #0 ;; set NZP code
    BRnz STORE ;; if R3(arrLength) is negative or zero, branch to STORE
    
    LDR R1, R4, #0 ;; R1 = men[R4] = ARR[i]
    
    BRn FLIPSIGN ;; if currNum < 0, go to FLIPSIGN
BACK
    ADD R0, R0, R1 ;; answer = answer + currNum
    ADD R4, R4, #1 ;; move address pointer of ARR
    ADD R3, R3, #-1
    BR WHILE
    
FLIPSIGN
    NOT R1, R1
    ADD R1, R1, #1
    BR BACK
    
    
STORE
    STI R0, ANSWER
    
    
HALT

;; Do not rename or remove any existing labels
LEN      .fill 5
ARR      .fill x6000
ANSWER   .fill x3050
.end

;; Answer needs to be stored here
.orig x3050
.blkw 1
.end

;; Array. Change values here for debugging!
.orig x6000
    .fill -3
    .fill 4
    .fill -1
    .fill 10
    .fill -20
.end
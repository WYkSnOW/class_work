#!/usr/bin/env python
# coding: utf-8

# # CS3630 Project 1: Trash Sorting Robot (Spring 2024)
# ## Brief
# - Due: Monday, Feb 5 at 11:59pm on Gradescope
# - Hand-in: through Gradescope
# 
# ## Getting started
# In order to use a file as your own, once we give you the notebook link:
# 1. Download the file to your computer
# 2. Upload the notebook to Google Colab (File > Upload Notebook)
# 
# ## Submission Instructions
# In order to submit a file, once you complete the project:
# 1. **Comment out !pip install statements (highlighted where necessary)**
# 2. Click the “File” button on the toolbar at the top
# 3. Click “Download,”
# 4. And then click “Download .py”
# 5. You will now have the .py file on your local machine.
# 6. Make sure it is named `project1.py`
# 7. Submit the `project1.py` file to gradescope (Make sure the file name matches exactly, else you will get an error on Gradescope)
# 
# 
# ## Introduction
# 
# In this project, we will be building a (simulated) trash sorting robot as illustrated in the [textbook](http://www.roboticsbook.org/intro.html) for this course. In this scenario, the robot tries to sort trash of some pre-determined categories into corresponding bins. Please refer to [Chapter 2](http://www.roboticsbook.org/S20_sorter_intro.html) of the book for a more detailed description of the scenario. **This project is basically based on Chapter 2 of the textbook. Please use the same values in the textbook for each TODO.**
# 
# **However, instead of using gtsam library, we will implement DiscreteDistribution, DiscreteConditional, and GaussianConditional classes ourselves. Please read the comments to understand each function.**

# First, import some useful libraries.

# In[151]:


#export
import numpy as np
import math
from enum import Enum


# In[152]:


get_ipython().run_line_magic('load_ext', 'autoreload')
get_ipython().run_line_magic('autoreload', '2')


# In[153]:


# Download the project1_test file to check your code locally on colab
### Comment out following line before submitting to Gradescope!!!
#! pip install --upgrade --no-cache-dir gdown
get_ipython().system(' gdown --id 1lQUdlp5Olsz4G3z3svjrc9AEYsktVdZ9') #Do NOT comment out


# In[154]:


from project1_test import TestProject1
from project1_test import verify


# In[155]:


np.random.seed(3630)
unit_test = TestProject1()


# **Useful Global Variables:**

# In[156]:


#export
class Trash(Enum):
    CARDBOARD = 0
    PAPER = 1
    CAN = 2
    SCRAP_METAL = 3
    BOTTLE = 4

# All possible actions/bins (nop means no action)
ACTIONS = ['glass_bin', 'metal_bin', 'paper_bin', 'nop']
# Categories of trash
Category = ['cardboard', 'paper', 'can', 'scrap_metal', 'bottle']
# Two conductivity states
Conductivity = ["false", "true"]
# Detection states
Detection = ['bottle', 'cardboard', 'paper']
# Appromixations of each category given 1000 pieces of trash
Appromixation = [200, 300, 250, 200, 50]


# **IMPORTANT NOTE: Please use the variables provided for the results of each of the TODOs.**
# ## Modeling the World State ([Book Section 2.1](http://www.roboticsbook.org/S21_sorter_state.html))
# - Functions to complete: **TODO 1 - TODO 4**
# - Objective: Representing the prior probabilities of the trash categories and simulate it by sampling. Please use the prior probabilities provided in the textbook

# **TODO 1:**
# 
# Complete DiscreteDistribution

# In[157]:


#export
# Probability distribution of a discrete variable.
class DiscreteDistribution:
  def __init__(self, prior_names, prior):
    '''
    Constructor
        Parameters:
            prior_names (list[str]): list of prior category names
            prior (list[float]): prior probablity/samples for each category
    '''
    self._names = prior_names.copy()
    self._prior = np.array(prior, dtype=float)
    self._prior /= self._prior.sum()

  def get_name_index(self, name) -> int:
    '''
    Helper function to get index of prior name
    '''
    return self._names.index(name)

  def pmf(self) -> list:
    return self._prior

  # TODO 1:
  # sample item
  def sample(self) -> int:
    '''
    Return a sample with the prior probabilities

        Parameters:
            None

        Returns:
            sampled_index (int): an int indicating the sampled item name,
                you may use the helper function to get index of a name
    '''
    sampled_index = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    sampled_index = np.random.choice(len(self._prior), p=self._prior)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return sampled_index


# In[158]:


# Sanity check to test your Discrete Distribution
print("Testing your Discrete Distribution implementation: ")
def local_test_discrete_distribution():
    categories = ['A', 'B', 'C']
    prior_probabilities = [0.3, 0.4, 0.3]

    distribution = DiscreteDistribution(prior_names=categories, prior=prior_probabilities)

    assert np.isclose(np.sum(distribution.pmf()), 1.0), "Probabilities do not sum to 1"

    sampled_value = distribution.sample()
    assert sampled_value in range(len(categories)), f"Sampled value {sampled_value} is out of range"

    category_to_check = 'B'
    index = distribution.get_name_index(category_to_check)
    assert index == categories.index(category_to_check), f"Index for {category_to_check} is incorrect"

    print("Sanity check passed successfully.")

local_test_discrete_distribution()


# **TODO 2 & TODO 3**:

# In[159]:


#export
# TODO 2:
# Prior probabilities
def get_category_prior():
    '''
    Returns the prior probabilities of the trash categories.

        Parameters:
            None

        Returns:
            category_prior (DiscreteDistribution): a DiscreteDistribution
                that summarizes the prior probabilities of all trash categories
    '''
    category_prior = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    prior = []
    for i in Appromixation:
        prior.append(i / 1000)
    category_prior = DiscreteDistribution(Category, prior)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return category_prior


# TODO 3:
# Prior probabilities PMF
def get_category_prior_pmf():
    '''
    Returns the probability mass function (PMF) of the prior probabilities
    of the trash categories.

        Parameters:
            None

        Returns:
            category_prior_pmf (list[float]): a list of the PMF
    '''
    category_prior_pmf = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    category_prior_pmf = get_category_prior().pmf()
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return list(category_prior_pmf)


# In[160]:


print("Testing your prior probabilities of the trash categories: ")
print(verify(unit_test.test_get_category_prior_pmf, get_category_prior_pmf))


# **TODO 4**:

# In[161]:


#export
# TODO 4:
def sample_category():
    '''
    Returns a sample of trash category by sampling with the prior probabilities
    of the trash categories

        Parameters:
            None

        Returns:
            sample (int): an int indicating the sampled trash category
    '''
    sample = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    sample = get_category_prior().sample()
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return sample


# In[162]:


print("Testing your sample of trash category: ", verify(unit_test.test_sample_category, sample_category))


# ## Sensors for Sorting Trash ([Book Section 2.3](http://www.roboticsbook.org/S23_sorter_sensing.html))
# - Functions to complete: **TODO 5-7** , **TODO 8-10**
# - Objective: Representing conditional probabilities of sensors and simulate them by sampling, please use the data provided in the textbook

# **TODO 5 & TODO 6:**
# 
# Complete DiscreteConditional

# In[163]:


#export
# Conditional probability of P(A | B), where both A and B are discrete variables.
class DiscreteConditional:
  def __init__(self, A_names, B_names, cpt):
    '''
    Constructor
        Parameters:
            A_names (list[str]): names of possible values of A
            B_names (list[str]): names of possible values of B
            cpt (list[list[float]]): conditional probability table, represented by a 2D list
    '''
    self._B_names = B_names.copy()
    self._A_names = A_names.copy()
    self._cpt = np.array(cpt, dtype=float)
    self._cpt /= np.sum(self._cpt, axis=1)[:, np.newaxis]

  def get_A_index(self, A_name) -> int:
    return self._A_names.index(A_name)

  # TODO 5:
  # sample value of A given value of B
  def sample(self, B_index: int) -> int:
    '''
    Returns a sample of A using the conditional probability distribution
    given the value of B

        Parameters:
            B_index(int): Given value of B (represented by an index)

        Returns:
            sampled_index (int): an int indicating the sampled item name,
                you may use the helper function to get index of A
    '''
    sampled_index = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    pmf = self._cpt[B_index,:]
    sampled_index = np.random.choice(len(pmf),p=pmf)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return sampled_index

  # TODO 6:
  # likelihoods of B given the value of A
  def likelihoods(self, A_index: int) -> list:
    '''
    Returns the likelihoods of all categories given the value of A

        Parameters:
            A_index (int): value of A (represented as an index)

        Returns:
            likelihoods (list[float] or np.ndarray): a list of likelihoods of each category
    '''
    likelihoods = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods = self._cpt[:,A_index]
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return likelihoods


# In[164]:


# Sanity check to test your Discrete Conditional implementation
def local_test_discrete_conditional():
    B_categories = ['B0', 'B1']
    A_categories = ['A0', 'A1', 'A2']
    cpt_matrix = np.array([[0.2, 0.5, 0.3],
                           [0.6, 0.2, 0.2]])

    conditional_distribution = DiscreteConditional(A_categories,
                                                   B_categories,
                                                   cpt=cpt_matrix)

    assert np.allclose(np.sum(conditional_distribution._cpt, axis=1), 1.0), "Conditional probabilities do not sum to 1"

    B_index_to_sample = 0
    sampled_A_index = conditional_distribution.sample(B_index_to_sample)
    assert sampled_A_index in range(len(A_categories)), f"Sampled A index {sampled_A_index} is out of range"

    A_index_to_check = 1
    likelihoods = conditional_distribution.likelihoods(A_index_to_check)
    assert np.isclose(np.sum(likelihoods / np.sum(likelihoods)), 1.0), "Likelihoods do not sum to 1 for the specified A index"

    print("Sanity check passed successfully.")

local_test_discrete_conditional()


# **TODO 7 & TODO 8:**
# 
# Complete GaussianConditional

# In[165]:


#export
# Conditional probability of P(A | B), where B is a discrete
# variable, and A is a continuous variable under Gaussian distribution.
class GaussianConditional:
    def __init__(self, B_names, means, sigmas):
      '''
      Constructor
          Parameters:
              B_names (list[str]): list of prior category names
              means (list[float]): list of mean measurement given each category
              sigmas (list[float]): list of measurement standard deviation given each category
      '''
      self._B_names = B_names.copy()
      self._means = means
      self._sigmas = sigmas

    @staticmethod
    def Gaussian(x, mu=0.0, sigma=1.0):
      return np.exp(-0.5 * (x - mu) ** 2 / sigma ** 2) / np.sqrt(2 * np.pi * sigma ** 2)

    # TODO 7:
    # sample A given B
    def sample(self, B_index):
        '''
        Returns a sample of weight using the conditional probability given
        the prior name index.

            Parameters:
                B_index (int): given value of B (represented by an index)

            Returns:
                weight (float): a float indicating the sampled weight
        '''
        weight = None
        ###############################################################################
        #                             START OF YOUR CODE                              #
        ###############################################################################
        mu = self._means[B_index]
        sigma = self._sigmas[B_index]
        weight = np.random.normal(mu, sigma)
        ###############################################################################
        #                              END OF YOUR CODE                               #
        ###############################################################################
        return weight

    # TODO 8:
    # likelihoods of A given B
    def likelihoods(self, B_value: float) -> list:
      '''
      Returns the likelihoods of A given B

          Parameters:
              B_value (float): a float indicating the value of B

          Returns:
              likelihoods (list[float] or np.ndarray): a list of likelihoods of A
      '''
      likelihoods = None
      ###############################################################################
      #                             START OF YOUR CODE                              #
      ###############################################################################
      likelihoods = [self.Gaussian(B_value, mu, sigma) for mu, sigma in zip(self._means, self._sigmas)]
      ###############################################################################
      #                              END OF YOUR CODE                               #
      ###############################################################################
      return likelihoods


# **TODO 9 - TODO 11**:
# 
# Declare conditional objects for sensor data

# In[166]:


#export
# TODO 9:
# 1. Conductivity - binary sensor
def get_pCT():
    '''
    Returns P(Conductivity | Trash Category)

        Parameters:
            None

        Returns:
            pCT (DiscreteConditional): a DiscreteConditional that
                indicates the conditinal probability of conductivity given
                the trash category
    '''
    pCT = None
    prob_distribution = []
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    prob_distribution = [
    [0.99, 0.01],
    [0.99, 0.01],
    [0.1, 0.9],
    [0.15, 0.85],
    [0.95, 0.05]
    ]
    pCT = DiscreteConditional(Conductivity, Category, prob_distribution)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return pCT


# TODO 10:
# 2. Detection - multi-valued sensor
def get_pDT():
    '''
    Returns P(Detection | Trash Category)

        Parameters:
            None

        Returns:
            pDT (DiscreteConditional): a DiscreteConditional that
                indicates the conditinal probability of camera detection
                given the trash category
    '''
    pDT = None
    prob_distribution = []
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    prob_distribution = [
    [0.02, 0.88, 0.1],
    [0.02, 0.2, 0.78],
    [0.33, 0.33, 0.34],
    [0.33, 0.33, 0.34],
    [0.95, 0.02, 0.03]
    ]
    pDT = DiscreteConditional(Detection, Category, prob_distribution)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return pDT

# TODO 11:
# 3. Weight - continuous-valued sensor
def get_pWT():
    '''
    Returns P(Weight | Trash Category)

        Parameters:
            None

        Returns:
            pWT (GaussianConditional): a GaussianConditional object which represents
            prior name, mean, and sigma of each category for Gaussian distribution
    '''
    pWT = None
    means = []
    sigmas = []
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    means = [20, 5, 15, 150, 300]
    sigmas = [10, 5, 5, 100, 200]
    pWT = GaussianConditional(Category, means, sigmas)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return pWT


# **TODO 12:**

# In[167]:


#export
# TODO 12:
def sample_conductivity(category=None):
    '''
    Returns a sample of conductivity using the conditional probability
    given the trash category.
    If the category parameter is None, sample a category first.

        Parameters:
            category (int): an int indicating the trash category

        Returns:
            conductivity (int): an int indicating the conductivity, with
                0 being nonconductive and 1 being conductive
    '''
    conductivity = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    # sample from category
    if category == None:
        category = sample()
    conductivity = get_pCT().sample(category)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return conductivity


# In[168]:


print("Testing your sample conductivity: ", verify(unit_test.test_sample_conductivity, sample_conductivity))


# **TODO 13**:

# In[169]:


#export
# TODO 13:
def sample_detection(category=None):
    '''
    Returns a sample of detection using the conditional probability given
    the trash category.
    If the category parameter is None, sample a category first.

        Parameters:
            category (int): an int indicating the trash category

        Returns:
            detection (int): an int indicating the sampled detection
    '''
    detection = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    if category == None:
        category = sample()
    detection = get_pDT().sample(category)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return detection


# In[170]:


print("Testing your sample detection: ", verify(unit_test.test_sample_detection, sample_detection))


# **TODO 14**:

# In[171]:


#export
# TODO 14:
def sample_weight(category=None):
    '''
    Returns a sample of weight using the conditional probability given
    the trash category.
    If the category parameter is None, sample a category first.

        Parameters:
            category (int): an int indicating the trash category

        Returns:
            weight (float): a float indicating the sampled weight
    '''
    weight = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    if category == None:
        category = sample()
    weight = get_pWT().sample(category)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return weight


# In[172]:


print("Testing your sample weight: ", verify(unit_test.test_sample_weight, sample_weight))


# ## Perception ([Book Section 2.4](http://www.roboticsbook.org/S24_sorter_perception.html))
# - Functions to complete: **TODO 15-19**
# - Objective: Calculating likelihoods using different methods given the observations from the world, please use the data provided in the textbook

# **TODO 15**:

# In[173]:


#export
# TODO 15:
def likelihood_no_sensors():
    '''
    Returns the likelihoods of all trash categories using only priors,
    aka no sensors.

        Parameters:
            None

        Returns:
            likelihoods (list[float]): a list of likelihoods of each trash category
    '''
    likelihoods = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods = get_category_prior_pmf()
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return likelihoods


# In[174]:


print("Testing your likelihoods with no sensors: ")
print(verify(unit_test.test_likelihood_no_sensor, likelihood_no_sensors))


# **TODO 16**:

# In[175]:


#export
# TODO 16:
def likelihood_given_weight(weight):
    '''
    Returns the likelihoods of all trash categories using only the weight
    sensor (no priors)

        Parameters:
            weight (float): a float indicating the weight of trash

        Returns:
            likelihoods (list[float] or np.ndarray): a list of likelihoods of each trash category
    '''
    likelihoods = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods = get_pWT().likelihoods(weight)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return likelihoods


# In[176]:


print("Testing your likelihoods using only the weight sensor: ")
print(verify(unit_test.test_likelihood_given_weight, likelihood_given_weight))


# **TODO 17**:

# In[177]:


#export
# TODO 17:
def likelihood_given_detection(detection):
    '''
    Returns the likelihoods of all trash categories using only the detection
    sensor (no priors)

        Parameters:
            detection (int): an int indicating the sampled detection

        Returns:
            likelihoods (list[float] or np.ndarray): a list of likelihoods of each trash category
    '''
    likelihoods = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods = get_pDT().likelihoods(detection)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return likelihoods


# In[178]:


print("Testing your likelihoods using only the detection sensor: ")
print(verify(unit_test.test_likelihood_given_detection, likelihood_given_detection))


# **TODO 18**:

# In[179]:


#export
# TODO 18:
def bayes_given_weight(weight):
    '''
    Returns the posteriors of all trash categories by combining the weight
    sensor and the priors

        Parameters:
            weight (float): a float indicating the weight of the trash

        Returns:
            posteriors (list[float] or np.ndarray): a list of posterior probabilities of each trash category
    '''
    posteriors = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods = likelihood_given_weight(weight)
    priors = get_category_prior_pmf()
    
    unnormalized_posteriors = np.multiply(likelihoods, priors)
    factor = sum(unnormalized_posteriors)
    posteriors = unnormalized_posteriors / factor
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return posteriors


# In[180]:


print("Testing your posteriors with the weight sensor and priors: ")
print(verify(unit_test.test_bayes_given_weight, bayes_given_weight))


# **TODO 19:**

# In[181]:


#export
# TODO 19:
# Bayes with three sensors
def bayes_given_three_sensors(conductivity, detection, weight):
    '''
    Returns the posteriors of all trash categories by combining all three
    sensors and the priors

        Parameters:
            conductivity (int): an int indicating the conductivity, with
                0 being nonconductive and 1 being conductive

            detection (int): an int indicating the sampled detection

            weight (float): a float indicating the weight of the trash

        Returns:
            posteriors (list[float] or np.ndarray): a list of posterior probabilities of each trash category
    '''
    posteriors = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    likelihoods_conductivity = get_pCT().likelihoods(conductivity)
    likelihoods_detection = likelihood_given_detection(detection)
    likelihoods_weight = likelihood_given_weight(weight)
    combined = likelihoods_conductivity * likelihoods_detection * likelihoods_weight
    
    priors = get_category_prior_pmf()
    
    unnormalized_posterios = combined * priors
    factor = sum(unnormalized_posterios)
    posteriors = unnormalized_posterios / factor
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return posteriors


# In[182]:


print("Testing your posteriors giving all three sensors: ")
print(verify(unit_test.test_bayes_given_three_sensors, bayes_given_three_sensors))


# ## Decision Theory ([Book Section 2.5](http://www.roboticsbook.org/S25_sorter_decision_theory.html))
# - Functions to complete: **TODO 20**
# - Objective: Incorporating the cost table with the perception to reach a final sorting decision

# In[183]:


# Cost table for each state
COST_TABLE = np.array([[2,  2,  4,  6,  0],
                       [1,  1,  0,  0,  2],
                       [0,  0,  5, 10,  3],
                       [1,  1,  1,  1,  1]])


# **TODO 20**:

# In[190]:


#export
# TODO 20:
### DECISION ###
def make_decision(posteriors):
    '''
    Returns the decision made by the robot given the likelihoods/posteriors you calculated

        Parameters:
            posteriors (list[float]): a list of posteriors of each trash category

        Returns:
            action (int): an int indicating the action taken by the robot
    '''
    action = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ##############################################################################
    expected_costs = COST_TABLE @ posteriors 
    action = np.argmin(expected_costs)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return action


# In[191]:


print("Testing the decision made by your robot: ")
print(verify(unit_test.test_make_decision, make_decision))


# In[192]:


unit_test.get_cost_table(COST_TABLE)
print("Testing your cost without sensors: ")
print(verify(unit_test.test_score_likelihood_no_sensor, likelihood_no_sensors, make_decision))
print("Testing your cost using the weight sensor:")
print(verify(unit_test.test_score_likelihood_given_weight, likelihood_given_weight, make_decision))
print("Testing your cost using the detection sensor:")
print(verify(unit_test.test_score_likelihood_given_detection, likelihood_given_detection, make_decision))
print("Testing your cost using with the weight sensor and priors:")
print(verify(unit_test.test_score_bayes_given_weight, bayes_given_weight, make_decision))
print("Testing your cost using all three sensors: ")
print(verify(unit_test.test_score_bayes_given_three_sensors, bayes_given_three_sensors, make_decision))


# ## Learning ([Book Section 2.6](http://www.roboticsbook.org/S26_sorter_learning.html))
# A Gaussian distribution, also known as a normal distribution, is an inappropriate distribution to represent
# the weight of an item. This is because it has an infinite range and therefore sampling from it can produce
# a negative number, while an item cannot have a negative weight. A more commonly used distribution
# used to represent weight is the [log-normal distribution](https://en.wikipedia.org/wiki/Log-normal_distribution) which can only contain positive real values. The book explains how to fit a gaussian distribution to a set of data.
# - Functions to complete: **TODO 21**
# - Objective: Fit a Log-Normal Distribution to a set of data
# - Hint: There is an estimation of parameters section on the wikipedia article

# **TODO 21**:

# In[205]:


#export
# TODO 21
def fit_log_normal(data):
    '''
    Returns mu, sigma for a log-normal distribution

        Parameters:
            data (list[float]): A list of positive floats that represent the weight of an item

        Returns:
            mu (float), sigma (float): The mu and sigma for a log-normal distribution
    '''
    mu = None
    sigma = None
    ###############################################################################
    #                             START OF YOUR CODE                              #
    ###############################################################################
    log_data = np.log(data)
    
    mu = sum(log_data) / len(log_data)
    
    var = np.var(log_data, ddof=1)
    sigma = math.sqrt(var)
    ###############################################################################
    #                              END OF YOUR CODE                               #
    ###############################################################################
    return mu, sigma


# In[206]:


print("Testing your log-normal distribution: ", verify(unit_test.test_fit_log_normal, fit_log_normal))


# In[ ]:





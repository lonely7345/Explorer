#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# Zeppelin documentation build configuration file
#
# This file is execfile()d with the current directory set to its containing dir.
#

import os
import sys
import xml.dom.minidom

try:
    sys.dont_write_bytecode = True
except:
    pass

sys.path.insert(0, os.path.abspath('ext'))


def child_node(node, name):
    for i in node.childNodes:
        if (i.nodeType == i.ELEMENT_NODE) and (i.tagName == name):
            return i
    return None


def node_text(node):
    return node.childNodes[0].data


def maven_version(pom):
    dom = xml.dom.minidom.parse(pom)
    project = dom.childNodes[0]

    version = child_node(project, 'version')
    if version:
        return node_text(version)

    parent = child_node(project, 'parent')
    version = child_node(parent, 'version')
    return node_text(version)


def get_version():
    version = os.environ.get('NOTEBOOK_VERSION', '').strip()
    return version or maven_version('../../../pom.xml')

# -- General configuration -----------------------------------------------------

needs_sphinx = '1.0'

#extensions = ['download']

templates_path = ['_templates']

source_suffix = '.rst'

master_doc = 'index'

project = u'Zeppelin'

copyright = u'2013, NFlabs'

version = get_version()
release = version

exclude_patterns = ['_build']

pygments_style = 'sphinx'

highlight_language = 'sql'

# -- Options for HTML output ---------------------------------------------------

html_theme_path = ['./templates']
html_theme = 'zeppelin'

html_title = '%s %s Documentation' % (project, release)

html_add_permalinks = None

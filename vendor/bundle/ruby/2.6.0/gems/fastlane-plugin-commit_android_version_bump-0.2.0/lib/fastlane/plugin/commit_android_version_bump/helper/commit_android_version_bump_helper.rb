module Fastlane
  module Helper
    class CommitAndroidVersionBumpHelper
      # class methods that you define here become available in your action
      # as `Helper::CommitAndroidVersionBumpHelper.your_method`
      #
      def self.show_message
        UI.message("Hello from the commit_android_version_bump plugin helper!")
      end
    end
  end
end
